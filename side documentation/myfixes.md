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

## Manifest file not found
If the manifest file is not found then you can reset it by:
```
If you see an error about AndroidManifest.xml, or some problems  
related to an Android zip file, right click on the project and select       
Android Tools > Fix Project Properties. (The project is looking in 
the wrong location for the library file, this will fix it for you.)  
```
From [here](http://stackoverflow.com/a/7451778)

## Getting the address as a string
solution from [here](http://stackoverflow.com/questions/16686436/getting-latitude-longitude-from-address-in-android)

```
//String addressStr = "faisalabad";/// this give me correct address
        String addressStr = "Sainta Augustine,FL,4405 Avenue A";
          Geocoder geoCoder = new Geocoder(MapClass.this, Locale.getDefault());

          try {
              List<Address> addresses =
          geoCoder.getFromLocationName(addressStr, 1); 
              if (addresses.size() >  0) {
                 latitude = addresses.get(0).getLatitude(); longtitude =
          addresses.get(0).getLongitude(); }

          } catch (IOException e) { // TODO Auto-generated catch block
          e.printStackTrace(); }


        pos = new LatLng(latitude, longtitude);
        googleMap.addMarker(new MarkerOptions().icon(
                BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(pos));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
```