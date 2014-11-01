## Move Android Files to New Project
These are instructions to move the important android files to a new project in Eclipse. 

Following, a rough degree, instructions specified [here](https://github.com/googlemaps/hellomap-android#import-the-project-to-your-workspace)

1. Download fresh copy of hello maps android starter code
2. Made new github repo and added Android project to that repo
3. Followed [these](https://developers.google.com/maps/documentation/android/start#create_an_api_project_in_the_google_apis_console) instructions to set up android API Key
4. Fixing “Google Maps cannot be resolved to a type” error
	5. Properties of Android Project, Andriod
	6. Add Library- make sure google play services_lib is open
	7. [screenshot to be inserted here]
8. Fixing "R cannot be resolved to a variable" 
	9. [screen shot of error]
	10. R is from R.java, a generated file when 	Android Manifest compiles and if there is no 	R.java, the manifest file is probably broken
	11. Add Library, make sure it is properly linked
	12. Screen shot
13. Ran App, it faile dthe first time added log.i statements
14. Ran again, it worked 
 

## Renaming Android Project & etc.
* Refactoring Eclipse Project, instructions: Refactor, Rename
* Refactoring Android Package Name => [here](http://stackoverflow.com/questions/3697899/package-renaming-in-eclipse-android-project/12429872#12429872)
