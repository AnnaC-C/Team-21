module TransactionsHelper
  # Validate the transfer and if possible, transfer the money.
  def transfer_money(to, from, amount)
    # TODO: Rails exception if fields are left blank, validate fields before calling this method.
    to_user_id = Account.find(to).user_id.to_i
    from_user_id = Account.find(from).user_id.to_i
    current_user_id = current_user.id.to_i

    # Parameter validation and error handling.
    if(to.to_i == from.to_i)
      # Make sure the 'to' and 'from' Accounts are unique.
      return {:status => -1, :message => "The account you're transferring from must not be the same as the
              account you're transferring to."}
    elsif(amount.to_i <= 0)
      # The Amount must be greater than 0.
      return {:status => -1, :message => "You can't transfer a null or negative amount of money."}
    elsif(to_user_id =! current_user_id || from_user_id =! current_user_id)
      # Both Accounts must be owned by the signed-in User.
      return {:status => -1, :message => "The current User must own all Accounts involved in a transfer."}
    end

    # Validate that the transfer can occur.
    @sender_account = Account.find(from)
    @receiver_account = Account.find(to)

    if(@sender_account.balance.to_i >= amount.to_i)
      # If there are sufficient funds, make the transfer.
      # Remove amount from the sender's account.
      @sender_account.balance -= amount.to_i
      @sender_account.save

      # Add amount to receiver's account.
      @receiver_account.balance += amount.to_i
      @receiver_account.save

      # Make a record of the transfer and return success.
      Transaction.create("sender_id" => from, "receiver_id" => to, "amount" => amount, "user_id" => current_user.id)
      return {:status => 0, :message => "Transfer complete."}
    else
      return {:status => -1, :message => "Insufficient funds to complete the transfer."}
    end
  end
end
