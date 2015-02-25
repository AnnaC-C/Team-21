module TransactionsHelper

  def validate_account_ownership(to, from)
    # Validate that the User owns both the :from and :to Account.

    # TODO: Rails exception if fields are left blank, validate fields before calling this method.
    to_user_id = Account.find(to).user_id.to_i
    from_user_id = Account.find(from).user_id.to_i
    current_user_id = current_user.id.to_i

    logger.info("User ID of TO: #{to_user_id}")
    logger.info("User ID of FROM: #{from_user_id}")
    logger.info("Current User ID: #{current_user_id}")

    if(to_user_id == current_user_id && from_user_id == current_user_id)
      logger.info("OWNER")
      return true
    else
      logger.info("NOT OWNER")
      return false
    end
  end

  def transaction_possible?
    # Ensure that the transaction can take place (i.e. sufficient balances in both accounts).
  end
end
