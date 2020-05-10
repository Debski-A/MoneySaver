import React from 'react';
import { Route, Switch } from 'react-router-dom';
import HomePage from '../pages/HomePage'
import RegisterPage from '../pages/RegisterPage'
import LoginPage from '../pages/LoginPage';
import LogoutRedirect from '../pages/LogoutRedirect'
import MainPage from '../pages/MainPage';
import AuthenticatedRoute from '../components/AuthenticatedRoute';
import { Redirect } from 'react-router-dom'

const PageConent = (props) => {
  return (
    <>
      <Switch>
        <Route exact path="/" component={HomePage} />
        <Route path="/register" render={(routeProps) => <RegisterPage {...routeProps} isLoggedIn={props.isLoggedIn} /> }/>
        <Route path="/login" render={(routeProps) => {
          return (
            props.isLoggedIn ? (
              <Redirect to='/' />
            ) : (
                <LoginPage {...routeProps} handleIsLoggedIn={props.handleIsLoggedIn} />
              )
          )
        }} />
        <Route path="/logout" render={(routeProps) => <LogoutRedirect {...routeProps} handleIsLoggedIn={props.handleIsLoggedIn} />} />
        <AuthenticatedRoute path="/main" component={MainPage} />
      </Switch>
    </>
  );
}

export default PageConent;