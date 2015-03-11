#PetVacayWeekOne
First sprint before merging with rest of the team members



The Profile view has the following : A. User is not logged in

* [x] PetVacay logo is displayed at the top.Clicking on this takes the User to the Login screen Right below the          above,User sees messages icon, wishlist icon and settings icon and with the latter 2 disabled/greyed out

* [x] Below the above icons, User can click on “How it works?” and this takes him/her to a separate view with            options to:                                                           
     a) Sign Up and Log in at the bottom of the screen
     b) Skip on top right, which takes them to Home view                                                       
     c) Go Back on top left, which takes them to Home view                                         

 * [x] Below the above, User can click on “List your space” and it takes him/her to login screen
      *  For now the user is taken to a a screen to take a picture from their camera or upload from gallery
      *  This will later be moved out and integrated into the appropriate place
      *  Did not have time to record this on the phone, so the GIF shows the use of the emulator
 
* [x] Below the above, User can click on “Help” and is taken to a separate “Help”screen( details of this screen          still need to be chalked out)

* [x] At the bottom of the page, User can click on “Log In or Sign Up” which takes them to Login screen


Video Walkthrough:

![Video Walkthrough](SprintOne.gif) 

Things that need tweaking:
*  Log In screen is pending
*  Resize images to retain aspect ratio
*  Change text title, content and background color of buttons when swiping through(replace fragments with             activities and then use view pager to do this)
* Get rid of action bar and replace with View pager
* Add divider between “Sign Up” and “Login”
* Lock navigation drawer when after the item in the list view is clicked on
* Actual push of the picture to the user table in Parse
* Giving the user an option to remove/delete pictures just taken/uploaded fomr gallery
* Code at https://github.com/anubharadwaj/PetVacayWeekOne.git
