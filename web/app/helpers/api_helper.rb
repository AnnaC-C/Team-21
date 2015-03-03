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
end
