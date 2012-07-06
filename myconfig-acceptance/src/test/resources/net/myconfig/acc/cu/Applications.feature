Feature: I want to manage my applications

Scenario: Creating an application

	Given I am on the list of applications
	When I create the app1 application
	Then I should see the app1 application in the list
