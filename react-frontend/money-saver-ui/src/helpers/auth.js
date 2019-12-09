import Cookies from 'js-cookie'

export const getAccessToken = () => Cookies.get('access_token')
export const getRefreshToken = () => Cookies.get('refresh_token')

export async function isAuthenticated () {
  if (!!getAccessToken()) return true;
  else if (getRefreshToken()) {
    try {
      const refreshToken = getRefreshToken()
      const tokens = await refreshTokens(refreshToken) // call an API, returns tokens
      if (tokens.error !=='undefined' ) return false

      const expires = (tokens.expires_in || 60 * 60) * 1000
      const inOneHour = new Date(new Date().getTime() + expires)

      // you will have the exact same setters in your Login page/app too
      Cookies.set('access_token', tokens.access_token, { expires: inOneHour })
      Cookies.set('refresh_token', tokens.refresh_token)

      return true
    } catch (error) {
      console.log(error)
      return false
    }
  }
  return false
}

function refreshTokens(refreshToken) {
  let apiBaseUrl = "http://localhost/api/auth/oauth/token";
  let refreshTokenCredentials = new FormData()
  refreshTokenCredentials.append('grant_type', 'refresh_token')
  refreshTokenCredentials.append('refresh_token', refreshToken)
  let basicAuthResult = btoa("browser:")
  const result = fetch(apiBaseUrl, {
    method: "POST",
    body: refreshTokenCredentials,
    headers: {
      'Authorization': 'Basic ' + basicAuthResult
    }
  })
    .then(response => response.json())
  return result
}
