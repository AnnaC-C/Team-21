module TransfersHelper
  # Validate the transfer and if possible, transfer the money.
  def transfer_money(to, from, amount)
    # Validate the parameter types. The following three lines will set the
    # variables to nil if they cannot be parsed as Integers/Float.
    to = Integer(to) rescue nil
    from = Integer(from) rescue nil
    amount = Float(amount) rescue nil

    # If any of the parameters ended up nil, they are invalid.
    if(to.nil? || from.nil? || amount.nil?)
      return {:success => false, :message => "Invalid input."}
    end

    # Round the amount to 2 decimal places.
    amount = (amount.to_f).round(2)

    to_user_id = Account.find(to).user_id.to_i
    from_user_id = Account.find(from).user_id.to_i
    current_user_id = current_user.id.to_i

    # Parameter validation and error handling.
    if(to.to_i == from.to_i)
      # Make sure the 'to' and 'from' Accounts are unique.
      return {:success => false, :message => "The account you're transferring from must not be the same as the
              account you're transferring to."}
    end

    if(amount.to_f <= 0.0)
      # The Amount must be greater than 0.
      return {:success => false, :message => "You can't transfer a null or negative amount of money."}
    end

    if(to_user_id != current_user_id || from_user_id != current_user_id)
      # Both Accounts must be owned by the signed-in User.
      return {:success => false, :message => "The current User must own all Accounts involved in a transfer."}
    end

    # Validate that the transfer can occur.
    @sender_account = Account.find(from)
    @receiver_account = Account.find(to)

    if(@sender_account.balance.to_f >= amount.to_f)
      # If there are sufficient funds, make the transfer.
      # Remove amount from the sender's account.
      @sender_account.balance -= amount.to_f
      @sender_account.save

      # Add amount to receiver's account.
      @receiver_account.balance += amount.to_f
      @receiver_account.save

      # Make a record of the transfer and return success.
      Transfer.create("sender_id" => from, "receiver_id" => to, "amount" => amount, "user_id" => current_user.id)
      return {:success => true, :message => "Transfer complete."}
    else
      return {:success => false, :message => "Insufficient funds to complete the transfer."}
    end
  end
end
