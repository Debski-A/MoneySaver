import React, { Component } from 'react';
import { Router } from 'react-router-dom';
import Navigation from './Navigation';
import Page from './Page';
import { createBrowserHistory } from "history";
import { isAuthenticated } from '../helpers/authenticationUtils'

export const history = createBrowserHistory()

class App extends Component {

  state = {
    isLoggedIn: isAuthenticated()
  }

  handleIsLoggedIn = () => {
    console.log('handleIsLoggedIn invoked after successful login...')
    this.setState ({
      isLoggedIn: isAuthenticated()
    })
  }

  render() {
  return (
    <Router history={history}>
      <div className="app">
        <header>
          <h1>Some Header</h1>
        </header>
        <main>
          <aside>
            <Navigation isLoggedIn={this.state.isLoggedIn}/>
          </aside>
          <section>
            <Page handleIsLoggedIn={this.handleIsLoggedIn.bind(this)} isLoggedIn={this.state.isLoggedIn} />
          </section>
        </main>
        <footer>
          <h1>Some footer</h1>
        </footer>
      </div>
    </Router>
  )
}
}

export default App;
