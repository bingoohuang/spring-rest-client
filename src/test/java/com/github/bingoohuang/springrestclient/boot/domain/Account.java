package com.github.bingoohuang.springrestclient.boot.domain;

public class Account {
    int money;
    String name;

    public Account() {
    }

    public Account(int money, String name) {
        this.money = money;
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (money != account.money) return false;
        return !(name != null ? !name.equals(account.name) : account.name != null);

    }

    @Override
    public int hashCode() {
        int result = money;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "money=" + money +
                ", name='" + name + '\'' +
                '}';
    }
}
