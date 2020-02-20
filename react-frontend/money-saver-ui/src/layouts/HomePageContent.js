import React from 'react';
import { Route, Switch } from 'react-router-dom'
import AddOutcomePage from '../pages/AddOutcomePage'
import AddIncomePage from '../pages/AddIncomePage'
import ShowIncomesAndOutcomes from '../pages/ShowIncomesAndOutcomes'
import AllIncomesAndOutcomes from '../pages/visualisations/AllIncomesAndOutcomes'

const HomePageContent = ({ currencies, frequencies, incomeCategories, outcomeCategories }) => {
  return (
    <>
      <Switch>
        <Route path="/main/income/add" render={(routeProps) => <AddIncomePage {...routeProps} currencies={currencies} frequencies={frequencies} incomeCategories={incomeCategories} />} />
        <Route path="/main/outcome/add" render={(routeProps) => <AddOutcomePage {...routeProps} currencies={currencies} frequencies={frequencies} outcomeCategories={outcomeCategories} />} />
        <Route path="/main/budget/view" render={(routeProps) => <ShowIncomesAndOutcomes />} />
        <Route path="/main/visualization/1" render={(routeProps) => <AllIncomesAndOutcomes {...routeProps} currencies={currencies} />} />
      </Switch>
    </>
  );
}

export default HomePageContent;