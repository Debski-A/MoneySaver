import Cookies from 'js-cookie'
import { history } from '../layouts/App';

export const getAccessToken = () => Cookies.get('access_token')
export const getRefreshToken = () => Cookies.get('refresh_token')
export const isAuthenticated = async () => {
  let isTokenOk = await checkToken(getAccessToken())
  return isTokenOk;
} 

export const authenticate = async () => {
  if (getRefreshToken()) {
    try {
      const tokens = await refreshTokens(getRefreshToken()) // call an API, returns tokens

      if (!!tokens.error) {
        redirectToLogin()
        return false
      }

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

const checkToken = async (token) => {
  let apiBaseUrl = "http://localhost/api/auth/oauth/check_token"
  let data = new FormData()
  data.append('token', token)
  return await fetch(apiBaseUrl, {
    method: 'POST',
    body: data
  })
    .then(response => response.ok)
    .catch((err) => {
      return false
    });
}

const refreshTokens = async (refreshToken) => {
  let apiBaseUrl = "http://localhost/api/auth/oauth/token"
  let refreshTokenCredentials = new FormData()
  refreshTokenCredentials.append('grant_type', 'refresh_token')
  refreshTokenCredentials.append('refresh_token', refreshToken)
  let basicAuthResult = btoa("browser:")
  return fetch(apiBaseUrl, {
    method: "POST",
    body: refreshTokenCredentials,
    headers: {
      'Authorization': 'Basic ' + basicAuthResult
    }
  })
    .then(response => response.json())
}

const redirectToLogin = () => {
  history.push("/login")
}
