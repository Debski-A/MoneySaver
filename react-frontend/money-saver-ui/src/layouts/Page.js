import React from 'react';
import { Route, Switch } from 'react-router-dom';
import HomePage from '../pages/HomePage'
import RegisterPage from '../pages/RegisterPage'
import LoginPage from '../pages/LoginPage';
import AuthenticatedRoute from "../components/AuthenticatedRoute"

const Page = () => {
  return (
    <>
      <Switch>
        <Route path="/" exact component={HomePage} />
        <Route path="/register" component={RegisterPage} />
        <Route path="/login" component={LoginPage} />
        <AuthenticatedRoute path="/main" component={LoginPage} />
      </Switch>
    </>
  );
}

export default Page;