# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)
User.create(email: "test@astervo.com", password: "password", password_confirmation: "password")

Account.create(description: "ONE", balance: "1000", interest_rate: 8.2, user_id: 1)
Account.create(description: "TWO", balance: "200", interest_rate: 4.1, user_id: 1)
Account.create(description: "THREE", balance: "500", interest_rate: 10.2, user_id: 1)
Account.create(description: "FOUR", balance: "15000", interest_rate: 1.1, user_id: 2)

Question.create(question: "What is it called when you borrow money from a bank?", correct: "A loan", incorrect_1: "A bond", incorrect_2: "An account", incorrect_3: "A  theft")
Question.create(question: "What type of account earns you the most interest?", correct: "Savings account", incorrect_1: "Current account", incorrect_2: "Access account", incorrect_3: "Trade account")
Question.create(question: "What does per annum mean?", correct: "Per year", incorrect_1: "Per month", incorrect_2: "Per week", incorrect_3: "Per hour")
Question.create(question: "What does ISA stand for?", correct: "Individual Savings Account", incorrect_1: "Instant Savings Account", incorrect_2: "Interest Savings Account", incorrect_3: "Identity Secure Account")
Question.create(question: "What is a standing order?", correct: "A payment that leaves on a set basis", incorrect_1: "A desk where you have to stand", incorrect_2: "A banking managers request", incorrect_3: "A high interest account")
Question.create(question: "What is interest?", correct: "Money added to your account on a set basis", incorrect_1: "The amount you pay the bank", incorrect_2: "How banks advertise", incorrect_3: "The difference between your incomings and outgoings")
Question.create(question: "You use a budget to:", correct: "Keep track of your money", incorrect_1: "Change from one provider to another", incorrect_2: "Complain to your bank", incorrect_3: "Gain more interest")
Question.create(question: "What is a mortgage?", correct: "A type of loan to buy a house", incorrect_1: "A type of home insurance", incorrect_2: "A high interest savings account", incorrect_3: "A deputy branch manager")
Question.create(question: "What is an offset loan?", correct: "A loan guaranteed against something valuable", incorrect_1: "Borrowing money at a lower rate", incorrect_2: "A high interest savings account", incorrect_3: "A loan used to start a business")