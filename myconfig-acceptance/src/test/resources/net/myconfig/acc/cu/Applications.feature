Feature: I want to manage my applications

Scenario: Creating an application

	Given I am on the list of applications
		And an unique application name
	When I create the application
	Then I should see the application in the list

Scenario: Creating an application that already exists

	Given I am on the list of applications
		And an unique application name
	When I create the application
		And I create the application
	Then I should see the "[S-002] The application with name "%s" is already defined." application error
