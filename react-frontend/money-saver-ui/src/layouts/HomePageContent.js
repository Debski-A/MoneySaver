import React from 'react';
import { Route, Switch } from 'react-router-dom'
import AddOutcomePage from '../pages/AddOutcomePage'
import AddIncomePage from '../pages/AddIncomePage'
import AllIncomesAndOutcomes from '../pages/visualisations/AllIncomesAndOutcomes'

const HomePageContent = (props) => {
  return (
    <>
      <Switch>
        <Route  path="/main/income/add" component={AddIncomePage} />
        <Route  path="/main/outcome/add" component={AddOutcomePage} />
        <Route path="/main/visualization/1" component={AllIncomesAndOutcomes} />
      </Switch>
    </>
  );
}

export default HomePageContent;