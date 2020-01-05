import React, { Component } from 'react'
import i18n from '../helpers/i18n'
import {authenticate, isAuthenticated, getAccessToken } from '../helpers/authenticationUtils'

class AddOutcomePage extends Component {

    componentWillMount() {
        this.getOutcomeCategories()
    }

    getOutcomeCategories = () => {
        if (!isAuthenticated) authenticate()

        let outcomeCategoriesUrl = "http://localhost/api/accounts/outcome/all"
        let currentLanguage = i18n.language
        fetch(outcomeCategoriesUrl, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getAccessToken(),
                'Accept-Language': currentLanguage
            }
        })
        .then(response => response.json())
        .then(response => console.log(response))
        .catch(err =>  {
            console.log(err)
        })
    }

    render() {
        return (
            <h1>Outcome Page</h1>
        )
    }

    componentDidUpdate() {
        this.getOutcomeCategories()
    }
}

export default AddOutcomePage