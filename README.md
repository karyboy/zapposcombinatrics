zapposcombinatrics
==================

When giving gifts, consumers usually keep in mind two variables - cost and quantity. 
This java Application allows a user to submit two inputs: K (desired # of products) and N (desired dollar amount) and uses the 
Zappos API (http://developer.zappos.com/docs/api-documentation) to create a list of Zappos products whose combined values match as closely as possible to X dollars. 
For example, if a user entered 3 (# of products) and $150, the application would print combinations of 3 product items whose total value is closest to $150.

Requirements
============
This application uses the Resty java Rest Client. The jar is included in the source. You need to include it in the build path of the application.
