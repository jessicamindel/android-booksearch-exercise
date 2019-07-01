# Book Search App

Android app that leverages the [OpenLibrary API](https://openlibrary.org/developers/api) to search books and display cover images. This app is to be used as the base app for adding suggested extensions.

![Imgur](http://i.imgur.com/NJmF42Yl.png)

## Overview

The app does the following:

1. Fetch the books from the [OpenLibrary Search API](https://openlibrary.org/dev/docs/api/search) in JSON format
2. Deserialize the JSON data for each of the books into `Book` objects
3. Build an array of `Book` objects and notify the adapter to display the new data. 
4. Define a view holder so the adapter can render each book model. 

To achieve this, there are four different components in this app:

1. `BookClient` - Responsible for executing the API requests and retrieving the JSON
2. `Book` - Model object responsible for encapsulating the attributes for each individual book
3. `BookAdapter` - Responsible for mapping each `Book` to a particular view layout
4. `BookListActivity` - Responsible for fetching and deserializing the data and configuring the adapter

## Usage
This app is intended to be the base project on top of which new features can be added. To use it, clone the project and import it using the following steps:

![Imgur](http://i.imgur.com/joPKoTk.gif)

## Suggested Extensions

1. Use SearchView to search for books with a title
2. Show ProgressBar before each network request
3. Add a detail view to display more information about the selected book from the list
4. Use a share intent to recommend a book to friends

## Libraries

This app leverages two third-party libraries:

 * [Android AsyncHTTPClient](http://loopj.com/android-async-http/) - For asynchronous network requests
 * [Picasso](http://square.github.io/picasso/) - For remote image loading

## Concept Review

- What is context? When it is used? Why?
	- A `Context` is a liaison between an application and the environment it's running in. It informs the application about its available resources, and grants access to those resources, such as images, other applications, databases, etc.
	- We often use Contexts to pass information to a new item, like a `View` on an `Activity`.
- How do I setup a click handler for items in a `RecyclerView`?
	- In the `ViewHolder` for the items in a `RecyclerView`, implement an `onClick` method after implementing the `View.OnClickListener` interface.

```java
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(View itemView) {
            // Do any necessary setup
            ...

            itemView.setOnClickListener(this);
        }

        // Handles the row being being clicked
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            ...
        }
    }
```

- How do I change the title text in the `ActionBar`? How do I add menu items?
	- To change the title text...
		- We first retrieve the `ActionBar` via `getSupportActionBar()`, or by using a Toolbar of our own.
		- Then, we set the title of the `ActionBar` with `actionBar.setTitle(str)`.
	- To add menu items...
		- We configure a menu layout resource file with the items of our choosing. A few examples:

**Place inside of...**
```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools" tools:context=".BookSearchActivity">
   ...
</menu>
```

**Settings**
```xml
    <item android:id="@+id/action_settings" android:title="@string/action_settings"
        android:orderInCategory="100" app:showAsAction="never" />
```

**Search**
```xml
    <item android:id="@+id/action_search"
        android:orderInCategory="5"
        android:title="Search"
        android:icon="@android:drawable/ic_menu_search"
        app:showAsAction="always|collapseActionView"
        app:actionViewClass="android.support.v7.widget.SearchView" />
```

**Progress Bar**
```xml
    <item
        android:id="@+id/miActionProgress"
        android:title="Loading..."
        android:visible="false"
        android:orderInCategory="100"
        app:showAsAction="always"
        app:actionLayout="@layout/action_view_progress" />
```

**Share**
```xml
    <item
        android:id="@+id/miShare"
        app:showAsAction="ifRoom"
        android:title="Share"
        app:actionProviderClass="android.support.v7.widget.ShareActionProvider" />
```

		- In `onCreateOptionsMenu` in the `Activity`, inflate the menu item(s):

```java
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_layout_name, menu);
        MenuItem item = menu.findItem(R.id.item_id);
        ...
        return super.onCreateOptionsMenu(menu);
    }
```

- How can I make menu items clickable?
	- Hook into `onOptionsItemSelected` in the `Activity`, and filter the menu item clicked by its id.

```java
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.item_id) {
            return true;
        } else if ...

        return super.onOptionsItemSelected(item);
    }
```

- What is an intent? What are the two types and how do they differ?
	- An `Intent` is the fabric between two `Activity`s, with one `Activity` from which to go and one `Activity` to which to go.
	- We're not sure about the difference between implicit and explicit intents.
- What types of tasks can I do with an implicit intent?
- How do I communicate data between intents? to a child intent and back to the parent?
- How do I enable an arbitrary object to be passable via an intent?
- What’s the difference between Parcelable and Serializable?
