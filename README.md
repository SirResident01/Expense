# Expense
Project Name: Expense Tracker  The goal of the project is to create a user-friendly and intuitive Android application for personal finance management, where users can register, log in, add their expenses, analyze them using charts and graphs, as well as monitor expenses over time.
Main functions:
Registration and authorization:

The ability to register a new user with unique credentials.
Login and password authorization.
Storing user data in the local Room database.
Data isolation: each user sees only their expenses.
Cost accounting:

Adding expenses indicating the category, amount and date.
Editing expenses that have already been added.
Deleting expenses with confirmation.
Cost analysis:

Pie Chart:
Shows the share of each expense category.
Line Chart:
Displays expenses for a day, week, or month, depending on the selected time period.
The data is presented by day, which allows you to track trends.
Local database:

Storing user data and their expenses using Room.
Using relationships between tables (Foreign Key) to associate expenses with a specific user.
User-friendly interface:

Simple and minimalistic design.
Using graphical elements such as pie charts and line graphs to visualize data.
Adaptation of the interface for different screens and devices.
Technical features:
Application Architecture: MVVM (Model-View-ViewModel).
Database: A room with tables for users and their expenses.
Frameworks and libraries:
MPAndroidChart for building graphs and diagrams.
LiveData and ViewModel for managing data and updating it in real time.
Programming language: Java.
Main use cases:
Registration of a new user:

The user enters a username and password to create a new account.
Authorization:

The user enters a username and password.
The application checks the credentials and opens the expense management screen.
Adding an expense:

The user enters the category, amount, and date of the expense.
The expense is saved in the database with reference to the current user.
Cost analysis:

The user can see their expenses in terms of categories (pie chart) and time (line graph).
Deleting expenses:

When deleting an expense, a confirmation window appears to avoid accidental deletion.

![1](https://github.com/user-attachments/assets/209fa43d-c588-452e-97d9-e27d64d48cd7)
![2](https://github.com/user-attachments/assets/1bd56bc0-9739-48fe-aba9-397bd8f4dc13)
![3](https://github.com/user-attachments/assets/7f55ac34-4c1d-4827-b812-ca23757b14b2)
![4](https://github.com/user-attachments/assets/902ff366-982a-4c0b-aaa7-accade316e5f)
![5](https://github.com/user-attachments/assets/4d9a6fd9-1eba-48eb-8473-956da085fc7f)
![6](https://github.com/user-attachments/assets/35c3b698-c96b-4727-9278-758715ef3a0d)
![7](https://github.com/user-attachments/assets/10aa826e-f1fc-4aa8-941b-65588e9f830c)
