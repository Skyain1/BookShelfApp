# BookShelf [MVVM,Dagger&Hilt,Room,GSON,Coroutines]

The application allows you to read books in fb2 format, make bookmarks and track your statistics.

## Screenshots
Screenshot of each activity

<p align="center">
<img src="app/src/main/res/raw/screen_reader.jpg"  height="600"></img>
</p>

<p align="center">
<img  src="app/src/main/res/raw/screen_sign.jpg"  height="500"></img>
<img src="app/src/main/res/raw/screen_main.jpg" height="500" ></img>
<img src="app/src/main/res/raw/screen_profile.jpg" height="500"></img>

</p>

<p align="center">
<img src="app/src/main/res/raw/screen_bookshelf.jpg"  height="500"></img>
<img src="app/src/main/res/raw/screen_bookmarks.jpg" height="500" ></img>
<img src="app/src/main/res/raw/screen_chapters.jpg" height="500"></img>
</p>


## Structure

#### Model
The model is presented from the repository for authorization

#### View
View are classic activities
 
#### ViewModel
ViewModels contain the logic of their screens


## Hilt dependencies
Dependencies are declared in modules from the Dagger&Hilt
 - `DataModule`
