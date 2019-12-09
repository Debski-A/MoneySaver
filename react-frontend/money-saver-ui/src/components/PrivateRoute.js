import React from 'react'
import { Redirect, Route } from 'react-router-dom'
import { isAuthenticated } from '../helpers/auth'

export const PrivateRoute = ({ component: Component, ...rest }) => (
    <Route {...rest} render={(props) => (
      isAuthenticated() === true
        ? <Component {...props} />
        : <Redirect to={{
            pathname: '/login',
            state: { from: props.location }
          }} />
    )} />
  )