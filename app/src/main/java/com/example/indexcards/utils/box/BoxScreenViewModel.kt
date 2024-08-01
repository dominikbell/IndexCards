package com.example.indexcards.utils.box

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Card
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.data.Tag
import com.example.indexcards.data.TagCardCrossRef
import com.example.indexcards.data.TagWithCards
import com.example.indexcards.utils.AppViewModel
import com.example.indexcards.utils.UserPreferences
import com.example.indexcards.utils.state.CardDetails
import com.example.indexcards.utils.state.CardState
import com.example.indexcards.utils.state.UiCardWithTags
import com.example.indexcards.utils.state.emptyCard
import com.example.indexcards.utils.state.toCard
import com.example.indexcards.utils.state.toCardDetails
import com.example.indexcards.utils.state.TagDetails
import com.example.indexcards.utils.state.TagState
import com.example.indexcards.utils.state.UiColorState
import com.example.indexcards.utils.state.emptyTag
import com.example.indexcards.utils.state.toTag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/** ViewModel for the BoxScreen
 * - views box and edits it (via parent class)
 *
 * - handles navigation between view, edit, and train
 *
 * - shows boxWithTags and cardsWithTags
 *
 * - enables filtering by levelSelected
 * - enables filtering by tagSelected (-> tagWithCards)
 *
 * - has CardUiState to view and edit card
 * - has TagUiState to view and edit tag
 */
@OptIn(ExperimentalCoroutinesApi::class)
class BoxScreenViewModel(
    appRepository: AppRepository,
    savedStateHandle: SavedStateHandle,
    userPreferences: UserPreferences
) : AppViewModel(
    appRepository = appRepository,
    userPreferences = userPreferences
) {
    /** boxId from savedStateHandle to know which box it is */
    val boxId: Long = checkNotNull(savedStateHandle["boxId"])


    /** boxScreenState
     * used for navigating between view, edit, and train
     */
    var boxScreenState: BoxScreenState by mutableStateOf(BoxScreenState.VIEW)

    fun updateBoxScreenState(newState: BoxScreenState) {
        boxScreenState = newState
    }


    /** uiBoxWithTags
     * contains box and all its tags
     */
    val uiBoxWithTags: StateFlow<UiBoxWithTags> =
        appRepository.getBoxWithTagsStream(boxId = boxId)
            .filterNotNull()
            .map {
                UiBoxWithTags(
                    box = it.box,
                    tagList = it.tags,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = UiBoxWithTags()
            )


    /** uiCardWithTags
     * contains all cards together with their tags
     */
    val uiCardsWithTags: StateFlow<UiCardsWithTags> =
        appRepository.getAllCardsWithTagsOfBoxStream(boxId = boxId)
            .filterNotNull()
            .map {
                UiCardsWithTags(it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = UiCardsWithTags()
            )


    /** levelSelected
     * used for filtering by level and for training
     */
    val levelSelected = MutableStateFlow(-1)

    fun setLevelSelected(newLevel: Int) {
        if (newLevel == levelSelected.value) {
            levelSelected.update { -1 }
        } else {
            levelSelected.update { newLevel }
        }
    }

    fun resetLevelSelected() {
        levelSelected.update { -1 }
    }

    val searchTerm = MutableStateFlow("")

    fun setSearchTerm(newTerm: String) {
        searchTerm.update { newTerm }
    }

    fun resetSearchTerm() {
        searchTerm.update { "" }
    }


    /** For sorting */
    val sortedBy = MutableStateFlow<BoxScreenSorting>(BoxScreenSorting.DATE_DESC)

    fun setSortedBy(newSorting: BoxScreenSorting) {
        sortedBy.update { newSorting }
    }

    fun resetSortedBy() {
        sortedBy.update { BoxScreenSorting.DATE_DESC }
    }


    /** tagSelected
     * used for filtering the cards by tag
     * uiTagWithCards is a StateFlow that emits an empty list when the tagSelected
     * is the emptyTag and a list of cards with this tag otherwise
     */
    val tagSelected = MutableStateFlow(emptyTag)
    val uiTagWithCards: StateFlow<UiTagWithCards> = tagSelected
        .flatMapLatest {
            when (it) {
                emptyTag -> flow {
                    emit(
                        TagWithCards(
                            tag = emptyTag,
                            cards = listOf()
                        )
                    )
                }

                else -> appRepository.getTagWithCardsStream(tagSelected.value.tagId)
            }
        }
        .filterNotNull()
        .map {
            UiTagWithCards(
                tag = it.tag,
                cardList = it.cards,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = UiTagWithCards()
        )

    fun setTagSelected(newTag: Tag) {
        tagSelected.update {
            newTag
        }
    }

    fun resetTagSelected() {
        tagSelected.update {
            emptyTag
        }
    }

    /** trainingCounts
     * maybe a temporary feature; changes if the training up-/downgrades cards during training
     */
    val trainingCounts = MutableStateFlow(false)

    fun changeTrainingCounts() {
        trainingCounts.update { !trainingCounts.value }
    }

    fun changeTrainingCounts(newState: Boolean) {
        trainingCounts.update { newState }
    }

    /** trainingDirection
     * maybe a temporary feature; changes which side of the card is shown when training
     * true = original order (word is shown, meaning is revealed after turning over)
     */
    val trainingDirection = MutableStateFlow(true)

    fun changeTrainingDirection() {
        trainingDirection.update { !trainingDirection.value }
    }

    fun changeTrainingDirection(newState: Boolean) {
        trainingDirection.update { newState }
    }

    /** used for determining if cards of a level in a box exist when coming from a
     * notification that passes a startLevel */
    suspend fun getNumberOfCardsOfLevelInBox(level: Int): Int {
        return appRepository.getNumberOfCardsOfLevelInBox(boxId, level)
    }


    /** functions for training that up-/downgrade the level on a card */
    fun onCardCorrect(card: Card) {
        viewModelScope.launch {
            if (card.level < 4) {
                appRepository.upgradeLevelOnCard(card.cardId)
            }
        }
    }

    fun onCardIncorrect(card: Card) {
        viewModelScope.launch {
            if (card.level > 0) {
                appRepository.downgradeLevelOnCard(card.cardId)
            }
        }
    }


    /** currentCard
     * used to select a card to open the CardDialog
     */
    val currentCard = MutableStateFlow(emptyCard)

    fun setCurrentCard(card: Card) {
        currentCard.update { card }
    }

    private fun resetCurrentCard() {
        currentCard.update { emptyCard }
    }

    /** uiCardWithTags is a StateFlow that emits an empty list when the currentCard
     * is the emptyCard and a list of tags of this card otherwise
     */
    val uiCardWithTags: StateFlow<UiCardWithTags> = currentCard
        .flatMapLatest {
            when (it) {
                emptyCard -> flow {
                    emit(
                        CardWithTags(
                            card = emptyCard,
                            tags = listOf()
                        )
                    )
                }

                else -> appRepository.getCardWithTagsStream(currentCard.value.cardId)
            }
        }
        .filterNotNull()
        .map {
            UiCardWithTags(
                card = it.card,
                tagList = it.tags,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = UiCardWithTags()
        )


    /** cardUiState
     * sets the Ui for creating and editing a card
     */
    var cardUiState by mutableStateOf(CardState())

    fun setCardUiStateFromCurrentCard() {
        cardUiState = CardState(
            cardDetails = uiCardWithTags.value.card.toCardDetails(),
            tagList = uiCardWithTags.value.tagList,
        )
    }

    fun resetCardUiState() {
        cardUiState = CardState()
    }

    fun updateCardState(
        cardDetails: CardDetails = cardUiState.cardDetails,
        tagList: List<Tag> = cardUiState.tagList
    ) {
        cardUiState =
            CardState(
                cardDetails = cardDetails,
                tagList = tagList,
                isValid = validateInput(cardDetails),
                validWord = cardDetails.word.isNotBlank(),
                validMeaning = cardDetails.meaning.isNotBlank(),
            )
    }

    fun updateCardState(cardState: CardState) {
        cardUiState = cardState
    }

    private fun validateInput(
        uiState: CardDetails = cardUiState.cardDetails
    ): Boolean {
        return with(uiState) {
            word.isNotBlank() && meaning.isNotBlank()
        }
    }


    /** newCardId is the id a new card will get
     * is needed to save tags and memo before saving the actual card */
    var newCardId by mutableLongStateOf(-1)

    /** functions for saving and deleting a new card */
    fun saveCard(doReset: Boolean = false) {
        if (cardUiState.isValid) {
            viewModelScope.launch {
                val cardId =
                    if (cardUiState.cardDetails.id == (-1).toLong()) {
                        appRepository.getBiggestCardId() + 1
                    } else {
                        cardUiState.cardDetails.id
                    }
                updateCardState(cardUiState.cardDetails.copy(id = cardId, boxId = boxId))
                appRepository.upsertCard(cardUiState.cardDetails.toCard())
                for (tag in uiBoxWithTags.value.tagList) {
                    if (cardUiState.tagList.contains(tag)) {
                        saveTagToCard(cardId = cardId, tagId = tag.tagId)
                    } else {
                        deleteTagFromCard(cardId = cardId, tagId = tag.tagId)
                    }
                }

                if (doReset) {
                    resetCard()
                }
            }
        }
    }

    fun setBiggestCardId() {
        viewModelScope.launch {
            newCardId = appRepository.getBiggestCardId() + 1
        }
    }

    fun resetCard() {
        resetCurrentCard()
        resetCardUiState()
    }

    fun deleteCard(card: Card) {
        viewModelScope.launch {
            appRepository.deleteCard(cardId = card.cardId)
        }
        resetCard()
    }


    /** functions for adding and deleting a tag to/from a card
     * are used upon saving a new card and immediately when clicking
     * on a tag when editing an existing card */
    private suspend fun saveTagToCard(
        tagId: Long,
        cardId: Long = currentCard.value.cardId,
    ) {
        appRepository.upsertTagCardCrossRef(
            TagCardCrossRef(cardId = cardId, tagId = tagId)
        )
    }

    private suspend fun deleteTagFromCard(
        tagId: Long,
        cardId: Long = currentCard.value.cardId,
    ) {
        appRepository.deleteTagCardCrossRef(
            cardId = cardId, tagId = tagId
        )
    }


    /** tagUiState
     * used for the TagDialog where the tag is edited
     */
    var tagUiState by mutableStateOf(TagState())

    fun setTagUiState(tagDetails: TagDetails) {
        tagUiState = TagState(
            tagDetails = tagDetails,
            isValid = validateTagInput(tagDetails),
            validText = tagDetails.text.isNotBlank()
        )
    }

    fun resetTagUiState() {
        setTagUiState(TagDetails())
    }

    /** ColorUiState, needed since the ColorPicker is a bit picky */
    var colorUiState by mutableStateOf(UiColorState())

    fun setColor(color: String) {
        if (validateColor(color)) {
            colorUiState = UiColorState(color = color)
            setTagUiState(tagUiState.tagDetails.copy(color = color))
        } else {
            colorUiState = UiColorState()
            setTagUiState(tagUiState.tagDetails.copy(color = ""))
        }
    }

    private fun validateColor(color: String): Boolean {
        return (color.first() == '#' && (color.length == 7 || color.length == 9))
    }


    /** Functions for saving, updating, and deleting tags
     * are more complicated since a tag should be added automatically to the cardUiState
     * when it is being created from a cardDialog. For this we need to know the new cardId
     * to create the reference in the database.
     */
    fun saveNewTag(addToCard: Boolean) {
        setTagUiState(tagUiState.tagDetails.copy(color = colorUiState.color))

        if (tagUiState.isValid) {
            viewModelScope.launch {
                val tagId: Long = appRepository.getBiggestTagId() + 1
                setTagUiState(
                    tagUiState.tagDetails.copy(
                        id = tagId,
                        boxId = boxId,
                    )
                )
                appRepository.insertTag(tagUiState.tagDetails.toTag())

                /* If the tag was created when the cardDialog was open, it should be added to that card */
                if (addToCard) {
                    updateCardState(
                        tagList = cardUiState.tagList.plus(tagUiState.tagDetails.toTag())
                    )
                }

                resetTagUiState()
            }
        }
    }

    fun updateTag() {
        setTagUiState(tagUiState.tagDetails.copy(color = colorUiState.color))
        if (tagUiState.isValid) {
            /* If the tag was in the cards tagList before, it should be there after updating */
            if (cardUiState.tagList.map { it.tagId }.contains(tagUiState.tagDetails.id)) {
                updateCardState(
                    tagList = cardUiState.tagList.plus(tagUiState.tagDetails.toTag())
                )
            }
            viewModelScope.launch {
                appRepository.updateTag(tagUiState.tagDetails.toTag())
            }
        }
    }

    fun deleteTag() {
        viewModelScope.launch {
            appRepository.deleteTag(tagUiState.tagDetails.id)
        }
    }

    private fun validateTagInput(tagDetails: TagDetails): Boolean {
        return (tagDetails.text.isNotBlank() && tagDetails.color.isNotBlank())
    }

    /** For exporting to a CSV file
     * returns a string in which the first line holds the box name, topic, description, and tags (text and color)
     * and in the following lines each one card, their meaning and their tags separated by a comma
     * Warning: This only takes the first value emitted by the database, changes during
     * the process are not taken into consideration */
    var doneCollectingData by mutableStateOf(false)
    var csvString by mutableStateOf("")

    fun collectCSVString() {
        viewModelScope.launch {
            doneCollectingData = false
            var res = ""

            /** Write Box and tags */
            appRepository.getBoxWithTagsStream(boxId = boxId)
                .filterNotNull()
                .first()
                .also { boxWithTags ->
                    res += "Box:;"
                    res += boxWithTags.box.name + ";"
                    res += boxWithTags.box.topic + ";"
                    res += boxWithTags.box.description + ";"

                    res += "\n"
                    res += "Tags:;"

                    boxWithTags.tags.forEach { tag ->
                        res += tag.text + ";"
                        res += tag.color + ";"
                    }
                }

            res += "\n"

            /** Write Cards */
            appRepository.getAllCardsWithTagsOfBoxStream(boxId = boxId)
                .filterNotNull()
                .first()
                .forEach { cardWithTags ->
                    res += cardWithTags.card.word + ";"
                    res += cardWithTags.card.meaning + ";"
                    res += cardWithTags.card.notes + ";"
                    cardWithTags.tags.forEach { tag ->
                        res += tag.text + ";"
                    }

                    res += "\n"
                }

            doneCollectingData = true
            csvString = res
        }
    }
}