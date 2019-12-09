import React from 'react';
import { Route} from 'react-router-dom';
import { isAuthenticated } from '../helpers/authenticationUtils'
import AuthenticateBeforeRender from './AuthenticateBeforeRender'

const AuthenticatedRoute = ({
    component: Component,
    exact,
    path,
  }) => (
    <Route
      exact={exact}
      path={path}
      render={props =>
        isAuthenticated() ? (
          <Component {...props} />
        ) : (
          <AuthenticateBeforeRender render={() => <Component {...props} />} />
        )
      }
    />
  )

export default AuthenticatedRoute