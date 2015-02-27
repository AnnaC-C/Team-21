module ApiHelper
  def retrieve_accounts
    accounts = []
    current_user.accounts.each do |a|
        accounts.push({:id => a.id,
                       :type => a.description,
                       :balance => a.balance,
                       :interest => a.interest_rate})

      return accounts
    end
  end
end
