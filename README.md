# ShowMeMoreShopps <Version 0.8.5>
ChangeLOG in Project ShowMeMoreShopping_IT_TEST_UPGRADE

Debug MODE IS ON, turn OFF before release (email and pass must be NULL)

RELEASE 0.8.5 

1. 16.12.2021 - 19.12.2021 				4185 -> 4400 
- Redesigned editor products activity
- Added new screen for editing
- Changed type listView on RecyclerView
- Added side button when swipes on product (left | right)
- Added undo callback with animation
- Relised synchronization with DB (Needed fix)

2. 20.12.2021 - 22.12.2021				4465 -> 4724
- Redesigned info card about market
- Refactored action when clicking on icon shop
- Added Screen with comments and interactions
- Class Comment maked save-block with email (only this email can edit and delete comment)
- Comments have like and warns
- Added Button add comment
- Added actions on button in menu
- Actions comment on long touch context menu (delete, warn , edit)
- Fixed pos ItemClickListener on comments

3. 22.12.2021 -  27.12.2021				 ____ -> 5169
- Redesigned Auth fragment
- Redesigned Login Dialog 
- Added Forgot password action
- Redesigned Registration Dialog
- Not Changed type saving DB (User have saveInDB saveFromDB)
- Not Released All system saving in DB and FromDB
- Refactoring folders and files
- Changed animation pop-up messages 
- Redesigned view items products
- BugFix | Optimisation

4. 28.12.2021 - 31.12.2021
- Added new item in menu Settings
- Added action switching dark\light theme (statusbar, header, menubar)
- Added new item in Settings (About devs)
- Redesigned Auth fragment (all login and register)
- Redesigned Settings fragment
- Redesigned style dialog when clicking on market
- Relised DB sync with comments ( any user with verified email can post comment in market) 
- Depending with warn or likes on comment, user color can be changed (-3 < RED) (GREEN > 3) 
- Restyled buttons on dialog
- Maked rule creating account with sizeMaxMarkets = 0
- Changing this value via DB and communicating with Team SMMS
- Microfixes (removing unnecessary code,optimizing algoritms,folders, and etc.)
