import React from 'react'
import Cookies from 'js-cookie'
import  { Redirect } from 'react-router-dom'

const LogoutRedirect = (props) => {
    Cookies.remove('access_token')
    Cookies.remove('refresh_token')
    props.handleIsLoggedIn()
    return <Redirect to='/'  />
}

export default LogoutRedirect;