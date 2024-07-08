package com.example.indexcards.utils.box

import androidx.compose.runtime.getValue
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
import com.example.indexcards.utils.UserPreferences
import com.example.indexcards.utils.card.CardDetails
import com.example.indexcards.utils.card.CardState
import com.example.indexcards.utils.card.UiCardWithTags
import com.example.indexcards.utils.card.emptyCard
import com.example.indexcards.utils.card.toCard
import com.example.indexcards.utils.card.toCardDetails
import com.example.indexcards.utils.tag.emptyTag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
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
    private val userPreferences: UserPreferences
) : BoxViewModel(
    appRepository = appRepository,
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

    /** used for determining if cards of a level in a box exist when coming from a
     * notification that passes a startLevel */
    suspend fun getNumberOfCardsOfLevelInBox(level: Int): Int {
        return appRepository.getNumberOfCardsOfLevelInBox(boxId, level)
    }


    /** functions for training that up-/downgrade the level on a card
     *
     */
    suspend fun onCardCorrect(card: Card) {
        if (card.level < 4) {
            appRepository.upgradeLevelOnCard(card.cardId)
        }
    }

    suspend fun onCardIncorrect(card: Card) {
        if (card.level > 0) {
            appRepository.downgradeLevelOnCard(card.cardId)
        }
    }


    /** currentCard
     * used to select a card to open the CardDialog
     */
    val currentCard = MutableStateFlow(emptyCard)

    fun setCurrentCard(card: Card) {
        currentCard.update { card }
    }

    fun resetCurrentCard() {
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
                isValid = validateInput(cardDetails)
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


    /** functions for saving and deleting a new card */
    fun saveCard(doReset: Boolean = false) {
        if (validateInput(cardUiState.cardDetails)) {
            viewModelScope.launch {
                val cardId =
                    if (cardUiState.cardDetails.id == (-1).toLong()) {
                        appRepository.getBiggestCardId() + 1
                    } else {
                        cardUiState.cardDetails.id
                    }
                updateCardState(cardUiState.cardDetails.copy(id = cardId, boxId = boxId))
                appRepository.upsertCard(cardUiState.cardDetails.toCard())
                for (tag in cardUiState.tagList) {
                    saveTagToCard(cardId = cardId, tagId = tag.tagId)
                }

                if (doReset) {
                    resetCurrentCard()
                    resetCardUiState()
                }
            }
        }
    }

//
//    /** functions for saving and deleting an existing card */
//    fun saveCard() {
//        if (validateInput(cardUiState.cardDetails)) {
//            viewModelScope.launch {
//                updateCardState(cardUiState.cardDetails.copy(boxId = boxId))
//                appRepository.upsertCard(cardUiState.cardDetails.toCard())
//                resetCardUiState()
//            }
//        }
//    }

    suspend fun deleteCard(card: Card) {
        appRepository.deleteCard(cardId = card.cardId)
    }

    /** functions for adding and deleting a tag to/from a card
     * are used upon saving a new card and immediately when clicking
     * on a tag when editing an existing card */
    suspend fun saveTagToCard(
        tagId: Long,
        cardId: Long = currentCard.value.cardId,
    ) {
        appRepository.upsertTagCardCrossRef(
            TagCardCrossRef(cardId = cardId, tagId = tagId)
        )
    }

    suspend fun deleteTagFromCard(tagId: Long) {
        appRepository.deleteTagCardCrossRef(
            cardId = currentCard.value.cardId, tagId = tagId
        )
    }
}