package com.lydiatest.randomuser.extension

import com.lydiatest.randomuser.model.*

fun User.Companion.mock() = User(
    gender = "gender_mock",
    name = UserName(
        title = "title_mock",
        first = "firstname_mock",
        last = "lastname_mock"),
    location = UserLocation(
        street = "street_mock",
        city = "city_mock",
        state = "state_mock",
        postCode = 1),
    email = "email@mock.com",
    login = UserLogin(
        username = "username_mock",
        password = "password_mock",
        salt = "some_salt_mock",
        md5 = "some_md5_mock",
        sha1 = "some_sha1_mock",
        sha256 = "some_sha256_mock"),
    registered = 1192836162,
    dob = 1030053735,
    phone = "0405060708",
    cell = "0607080901",
    id = UserId(
        name = "userid_name_mock",
        value = "userid_value_mock"),
    picture = UserPicture(
        large = "large_picture_mock",
        medium = "medium_picture_mock",
        thumbnail = "thumb_mock"),
    nat = "nat_mock"
)