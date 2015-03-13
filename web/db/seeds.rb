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

Question.create(question: "1?", correct: "C", incorrect_1: "I", incorrect_2: "I", incorrect_3: "I")
Question.create(question: "2?", correct: "C", incorrect_1: "I", incorrect_2: "I", incorrect_3: "I")
Question.create(question: "3?", correct: "C", incorrect_1: "I", incorrect_2: "I", incorrect_3: "I")
Question.create(question: "4?", correct: "C", incorrect_1: "I", incorrect_2: "I", incorrect_3: "I")
Question.create(question: "5?", correct: "C", incorrect_1: "I", incorrect_2: "I", incorrect_3: "I")
Question.create(question: "6?", correct: "C", incorrect_1: "I", incorrect_2: "I", incorrect_3: "I")
Question.create(question: "7?", correct: "C", incorrect_1: "I", incorrect_2: "I", incorrect_3: "I")
Question.create(question: "8?", correct: "C", incorrect_1: "I", incorrect_2: "I", incorrect_3: "I")
Question.create(question: "9?", correct: "C", incorrect_1: "I", incorrect_2: "I", incorrect_3: "I")
Question.create(question: "10?", correct: "C", incorrect_1: "I", incorrect_2: "I", incorrect_3: "I")
