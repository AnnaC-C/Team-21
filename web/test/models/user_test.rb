require 'test_helper'

class UserTest < ActiveSupport::TestCase
  def setup
    @user = User.new(name: "Test User", email: "test@account.eu",
                     password: "password", password_confirmation: "password")
  end

  test "should be a valid user" do
    assert @user.valid?
  end

  test "should have a name" do
    @user.name = " "
    assert_not @user.valid?
  end

  test "should have an email" do
    @user.email = " "
    assert_not @user.valid?
  end

  test "name shouldn't be too long" do
    @user.name = "z" * 51
    assert_not @user.valid?
  end

  test "email shouldn't be too long" do
    @user.email = "z" * 244 + "test.se"
    assert_not @user.valid?
  end

  test "email field should accept these" do
    addresses = %w[test@account.com TEST@test.com U_EN-GB@acc.ount.com
     t.est@test.test te+st@test.com]

    addresses.each do |address|
      @user.email = address
      assert @user.valid?, "#{address.inspect} should be valid."
    end
  end

  test "email field should reject these" do
    addresses = %w[test@account,com TEST@te_st.com g.b@acc.ount.
     t.est@tes_est te+st@te+st.com]

    addresses.each do |address|
      @user.email = address
      assert_not @user.valid?, "#{address.inspect} should be invalid."
    end
  end

  test "emails should be unique" do
    duplicate = @user.dup
    duplicate.email = @user.email.upcase
    @user.save
    assert_not duplicate.valid?
  end

  test "password should have a minimum length" do
    @user.password = @user.password_confirmation = "z" * 5
    assert_not @user.valid?
  end
end
