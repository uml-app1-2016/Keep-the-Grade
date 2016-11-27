# Keep the Grade
An android application to keep track of your classes, including grades, class info, and analytics.

## Programmers (in no specific order)
Tim Larocque, Ashley Hale, Adam Gaudreau

# PROGRESS REPORT 2
In this week, we added the ability to delete a class, add a grade, and add a class with weights (i.e. 20% exams, 10% HW, etc.). We also made sure in our delete commands that it is recursive, so when you delete a semester, it deletes all classes and grades within that semester. In addition, when a grade is added, your current grade is calculated based off the weights entered when you add a class. Finally, there are some layout changes in the works to make it look nice and pretty.

ADAM
Class
ClassAdapter
Grade
DatabaseContract
DatabaseHelper
DatabaseUtils
MainActivity
Semester

TIM
ClassActivity
MainActivity
activity_class.xml
Pretty much all the XML layout files

ASHLEY
CategoryAdapter
Chart
ChartAdapter
ChartFragment
ClassActivity
Grades
MySingleton

