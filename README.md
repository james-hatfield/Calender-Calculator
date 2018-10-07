# Calender-Calculator
Calculates payable dates for employees

This program uses a gui to help the user navigate through their file system to find an excel file that contains date information specific
to the SUSD employee date structure. Once the file is chosen, the program parses all of the dates and displays the employee date numbers
that the user can select and run calculations on using what ever start dates their specific employee has to calculate their assessment
dates.

The user can load school specific holidays, furlow days, or breaks that do not need to be counted in the calculations. These exceptions
are displayed on their respective table and can be added or deleted as the user sees fit. 

This application uses the Apachi Poi to parse the excel spreadsheet. There is a known bug in versions of Java newer than Java 8 that 
that doesn't allow the API to function properly due to a way Java changed how reflection works in Java 9 and higher. Due to this bug,
this program must be run using Java8 in order for the parser to work until the bug is fixed for newer versions of Java.
