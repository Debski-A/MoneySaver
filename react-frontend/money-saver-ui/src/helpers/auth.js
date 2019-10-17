import Cookies from 'js-cookie'
import React from 'react'
import { Redirect } from 'react-router-dom'

export const getAccessToken = () => Cookies.get('access_token')
export const getRefreshToken = () => Cookies.get('refresh_token')
export const isAuthenticated = () => !!getAccessToken()

export const authenticate = async () => {
    if (getRefreshToken()) {
      try {
        const refreshToken = getRefreshToken()
        const tokens = await refreshTokens(refreshToken) // call an API, returns tokens
  
        const expires = (tokens.expires_in || 60 * 60) * 1000
        const inOneHour = new Date(new Date().getTime() + expires)
  
        // you will have the exact same setters in your Login page/app too
        Cookies.set('access_token', tokens.access_token, { expires: inOneHour })
        Cookies.set('refresh_token', tokens.refresh_token)
  
        return true
      } catch (error) {
        redirectToLogin()
        return false
      }
    }
  
    redirectToLogin()
    return false
  }

  const refreshTokens = (refreshToken) => {
    let apiBaseUrl = "http://localhost:6666/auth/oauth/token";
    let refreshTokenCredentials = {
      "grant_type": "refresh_token",
      "refresh_token": refreshToken
    }
    let basicAuthResult = btoa("browser")
    fetch(apiBaseUrl, {
      method: "POST",
      body: JSON.stringify(refreshTokenCredentials),
      headers: {
        'Authorization': 'Basic ' + basicAuthResult
      }
    })
    .then(response => response.json())
  }

  const redirectToLogin = () => {
    return (
      <Redirect to={{ pathname: '/login' }}/>
    )
  }