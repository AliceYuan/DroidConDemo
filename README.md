Droidcon Demo App
=====

The slides for the talk is available at: https://goo.gl/idKr6W

The video for talk is available at: http://uk.droidcon.com/skillscasts/10795-common-poor-coding-patterns-and-how-to-avoid-them

The demo app looks like this:  

![alt text](demo_app.gif)

It will go over the 4 problems which we will talk about in the slides. 


Checking out the code
=====

In order to check out the initial "bad" code do: 

`git checkout bad_code`

In order to check out the solution to problem 1-4: 

`git checkout problem<X>_solution`

eg. `git checkout problem1_solution`

In order to checkout the final solution code do:

`git checkout final_solution`



How to make app run
=====
*Note:* this app will not run without Pinterest PDK api key. 

Getting an API key should be fairly straight forward ![See docs here](https://developers.pinterest.com/docs/api/overview/). 

First, create a Pinterest Developer application.

Once you have created your first Pinterest developer account, you can copy the API key from the developer application. Paste it into the `app/src/main/res/values/strings.xml` file by replacing `you_api_key_here` with the API key you got from your developer application.

