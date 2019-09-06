# Unified Contact Picker Android Library

[![GitHub release](https://img.shields.io/github/release/quiin/UnifiedContactPicker.svg)](https://github.com/quiin/UnifiedContactPicker/releases/latest)  [![Min SDK](https://img.shields.io/badge/minSDK-15-brightgreen.svg)](https://developer.android.com/about/dashboards/index.html)  [![GitHub license](https://img.shields.io/badge/license-Apache%202-orange.svg)](https://raw.githubusercontent.com/quiin/UnifiedContactPicker/master/LICENSE)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Unified%20Contact%20Picker-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5158) 

## Introduction

This library unifies the user contacts in a compact and user intuitive way allowing the end-user to choose between the contact's available communication options (email/phone number) follows Material Design guidelines.

Although there is a standard way to call the contact list in Android, it does not always feel well-integrated in your app
Android applications.
UnifiedContactPicker is an Android library which allows you to easily integrate contact picking workflow into your application with minimal effort

### IMPORTANT :exclamation:
---
This library is no longer in active development. However, pull requests for new features or bugfixes are always welcomed and will be attended.

## Demo
![](https://media.giphy.com/media/26xBtXCT49aFwPmCI/source.gif)

## Features

* Unifies user's contacts
* Customizable UI
* Easy and redy to use
* Display list of contacts
* Intuitive interface
* Follows Material Design guidelines
* Asynchronous contact loading


# Installation

In order to use the library, add the following line to your **root** gradle file:
<pre> <code>
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
    ...
}
</code> </pre>

As well as this line in your project **build.gradle** file
<pre> <code>
dependencies {
    compile 'com.github.quiin:unifiedContactPicker:{LATEST_VERSION}'
    ...
}
</code> </pre>

# Usage

To use UnifiedContactPicker in your app simply follow this 3 simple steps:

1. Add _read contacts_ permission in your manifest

```
    <uses-permission android:name="android.permission.READ_CONTACTS"/>
```

2. Launch _ContactPickerActivity.java_ as activity result

```java
// Your Activity or Fragment
public void launchContactPicker(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            Intent contactPicker = new Intent(this, ContactPickerActivity.class);
            startActivityForResult(contactPicker, CONTACT_PICKER_REQUEST);
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_CONTACTS},
                    READ_CONTACT_REQUEST);
        }
    }

```

3. Override _onActivityResult()_  and wait for the user to select the contacts.

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode){
        case CONTACT_PICKER_REQUEST:
            if(resultCode == RESULT_OK){
                TreeSet<SimpleContact> selectedContacts = (TreeSet<SimpleContact>)data.getSerializableExtra(ContactPickerActivity.CP_SELECTED_CONTACTS);
                for (SimpleContact selectedContact : selectedContacts)
                        Log.e("Selected", selectedContact.toString());
            }else
                Toast.makeText(this, "No contacts selected", Toast.LENGTH_LONG).show();
        break;
        default:
            super.onActivityResult(requestCode,resultCode,data);
        }
}

```

Contacts are returned in a TreeSet of *SimpleContact*; each *SimpleContact* object ha the following accessible properties:

* DisplayName - Contact display name
* Communication - Contact selected communication (email/phone)

> :warning:  IMPORTANT
>
>  As of SDK 23 (Android 6) developers are requested to explicitly ask for permissions at runtime. So please be sure to request for contact reading permissions using the previous code or any other means you prefer.


## Customization

The following UI views can be customized:

| UI component           |       Intent extra        | Expected value |  Type   | Sugestion |
|:----------------------:|:-------------------------:|:--------------:|:-------:|:---------:|
| FAB color              | CP_EXTRA_FAB_COLOR        | hexColor       | String  |       -   |
| Selection color        | CP_EXTRA_SELECTION_COLOR  | hexColor       | String  |       -   |
| Selection Drawable     |CP_EXTRA_SELECTION_DRAWABLE| Image          | byte [] |use PickerUtils.sendDrawable()|
| Fab drawable           | CP_EXTRA_FAB_DRAWABLE     | Image          | byte [] |use PickerUtils.sendDrawable()|
| Chips                  | CP_EXTRA_SHOW_CHIPS       | boolean        | boolean |       -   |


Aditionally, you can customize the contact query parameters used to extract the user's contacts adding the following extras to the intent

| Extra                             |   Type    |
|:---------------------------------:|:---------:|
|CP_EXTRA_PROJECTION                | String [] |
|CP_EXTRA_SELECTION                 | String    |
|CP_EXTRA_SELECTION_ARGS            | String [] |
|CP_EXTRA_HAS_CUSTOM_SELECTION_ARGS | boolean   |
|CP_EXTRA_SORT_BY                   | String    |

#### Example
```java
public void launchContactPicker(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            Intent contactPicker = new Intent(this, ContactPickerActivity.class);
            //Don't show Chips
            contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_SHOW_CHIPS, false);
            //Customize Floating action button color
            contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_FAB_COLOR, "#FFF722");
            //Customize Selection drawable
            contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_SELECTION_DRAWABLE, PickerUtils.sendDrawable(getResources(),R.drawable.my_drawable));
            startActivityForResult(contactPicker, CONTACT_PICKER_REQUEST);
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_CONTACTS},
                    READ_CONTACT_REQUEST);
        }
    }
```

## Default values & behavior

* Default projection columns:
  * ContactsContract.Data._ID
  * ContactsContract.Contacts.DISPLAY_NAME
  * ContactsContract.CommonDataKinds.Phone.NUMBER
  * ContactsContract.Data.MIMETYPE

* Default selection query:
```java
"(" + ContactsContract.Data.MIMETYPE + "=? OR " + ContactsContract.Data.MIMETYPE + "=?)"
```
* Default selection params:
  * ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
  * ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
* Default sorting: DisplayName Ascendant
* A chip is added to the textview when one of the following happens:
  * User chooses a contact from the contact list
  * User writes a new email/phone - the chip is created after an empty space is found (" ")

## Considerations
* In the absence of any of these extras, its value will fallback to the default value
* If you wish to use a custom selection string (CP_EXTRA_SELECTION) with custom selection arguments (CP_EXTRA_SELECTION_ARGS) the use of CP_EXTRA_HAS_CUSTOM_SELECTION_ARGS is _required_ in order for the query to work (see defaults section for more information)


## Support & extension
- Feel free to make any pull request to add a new behaviour or fix some existing bug
- Feel free to open issues if you find some bug or unexpected behaviour
- I'll keep polishing and giving support to this library in my free time

## Acknowledgments

 * [MaterialLetterIcon](https://github.com/IvBaranov/MaterialLetterIcon) For the cool image icon in contact list
 * [RoundedImageView](https://github.com/vinc3m1/RoundedImageView) For the rounded icon once a contact is selected
 * [ExpandableRecyclerView](https://github.com/bignerdranch/expandable-recycler-view) For the expandable recycler view in contact list
 * [Nachos](https://github.com/hootsuite/nachos) For the chip view
