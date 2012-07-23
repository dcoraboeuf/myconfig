Feature: General management of the console

Scenario: List of languages

	Given I am on the list of applications
	Then I should see the following languages
		| EN |
		| FR |

Scenario: Selecting French

	Given I am on the list of applications
	When I select "FR" as a language
	Then the page title is "Applications configurées"
	
Scenario: Selecting English
	
	Given I am on the list of applications
	When I select "EN" as a language
	Then the page title is "Configured applications"
	