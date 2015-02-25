module TransactionsHelper

  def validate_account_ownership(to, from)
    # Validate that the User owns both the :from and :to Account.

    # TODO: Rails exception if fields are left blank, validate fields before calling this method.
    to_user_id = Account.find(to).user_id.to_i
    from_user_id = Account.find(from).user_id.to_i
    current_user_id = current_user.id.to_i

    logger.info("User ID of RECEIVING ACCOUNT: #{to_user_id}")
    logger.info("User ID of SENDER ACCOUNT: #{from_user_id}")
    logger.info("Current User ID: #{current_user_id}")

    if(to_user_id == current_user_id && from_user_id == current_user_id)
      logger.info("OWNERSHIP SUCCESS")
      return true
    else
      logger.info("OWNERSHIP FAILURE")
      return false
    end
  end

  def transaction_possible?(from, amount)
    # Ensure that the transaction can take place (i.e. sufficient balances in both accounts).

    logger.info("AMOUNT: £ #{amount}")

    sender_account_balance = Account.find(from).balance.to_i
    logger.info("SENDER BALANCE: £ #{sender_account_balance}")

    result = sender_account_balance >= amount.to_i
    logger.info("POSSIBLE: #{result}")
    return result
  end

  def transfer_money(to, from, amount)
    # Transfer money from one account to another.
    # TODO: Move both validation methods above into here.
    # TODO: API flag?
    @sender_account = Account.find(from)
    @receiver_account = Account.find(to)
    # Remove amount from the sender's account.
    @sender_account.balance -= amount.to_i
    @sender_account.save

    # Add amount to receiver's account.
    @receiver_account.balance += amount.to_i
    @receiver_account.save

    # TODO: Current User's ID will be different for API accesses.
    Transaction.create("sender_id" => from, "receiver_id" => to, "amount" => amount, "user_id" => current_user.id)
  end
end
