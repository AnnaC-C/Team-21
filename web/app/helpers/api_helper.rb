module ApiHelper

  # Returns all of a User's Accounts as an array of JSON objects.
  def retrieve_accounts
    accounts = []
    # For each Account, push an object with
    # its ID, balance, and interest to the array.
    current_user.accounts.each do |a|
        accounts.push({:id => a.id,
                       :type => a.description,
                       :balance => a.balance,
                       :interest => a.interest_rate})
    end
    return accounts
  end

  # Returns all of a User's Transfers as an array of JSON objects.
  def retrieve_transfers
    transfers = []

    # For each Transfer, push a JSON object with its sender ID, reciever ID,
    # and balance to the array.
    current_user.transfers.each do |t|
      transfers.push({:sender_id => t.sender_id,
                      :receiver_id => t.receiver_id,
                      :amount => t.amount,
                      :date => t.created_at})
    end
    return transfers
  end
end
