# IndexCards

IndexCards is a simple app for learning anything, in particular languages, using the index card learning principle.

## Screenshots

A picture say more than a thousand words. The home screen contains an overview of all boxes:

<table>
  <tr>
    <td align="center">
      <img src="res/homescreen_empty.png" width="200px;"/><br />
      <sub><b>When first opening the app.</b></sub>
    </td>
    <td align="center">
      <img src="res/homescreen_filled.png" width="200px;"/><br />
      <sub><b>The home screen after adding several boxes.</b></sub>
    </td>
  </tr>
</table>


## Features

- Learning languages 
- Tutorial
- Reminders for training as notifications
- User preferences
- Exporting to and importing from CSV files

## Languages

The UI of the app is available in English and German. There following languages are supported with flags to select as a language box:

- Albanian
- Arabic
- Chinese
- Croatian
- Danish
- English
- Estonian
- Finnish
- French
- German
- Greek
- Hebrew
- Hungarian
- Icelandic
- Italian
- Japanese
- Korean
- Latin
- Norwegian
- Persian
- Portuguese
- Russian
- Spanish
- Swedish
- Turkish

## Technical Details

This app is written entirely in [Kotlin](https://kotlinlang.org/). It uses [Compose](https://developer.android.com/compose) for the UI and [Room](https://developer.android.com/training/data-storage/room/) for persistent storage of data. Other modules used are the [colopicker-compose by skydoves](https://github.com/skydoves/colorpicker-compose) and [datastore](https://developer.android.com/topic/libraries/architecture/datastore) for storing preferences.

Boxes, cards, tags, and categories are stored in an SQLite database.
