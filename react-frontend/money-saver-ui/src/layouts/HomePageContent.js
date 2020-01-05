import React from 'react';
import { Route, Switch } from 'react-router-dom'
import AddOutcomePage from '../pages/AddOutcomePage'
import AddIncomePage from '../pages/AddIncomePage'

const HomePageContent = (props) => {
  return (
    <>
      <Switch>
        <Route  path="/main/income/add" component={AddIncomePage} />
        <Route  path="/main/outcome/add" component={AddOutcomePage} />
        <Route path="/main/visualization/1" component={null} />
      </Switch>
    </>
  );
}

export default HomePageContent;