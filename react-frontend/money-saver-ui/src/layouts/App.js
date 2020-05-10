import React, { Component } from 'react';
import { Router } from 'react-router-dom';
import Navigation from './Navigation';
import PageContent from './PageContent';
import { createBrowserHistory } from "history";
import { isAuthenticated } from '../helpers/authenticationUtils'
import 'bootstrap/dist/css/bootstrap.min.css' //required
import '../styles/App.css'
import coin from '../images/coin.jpg'
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

export const history = createBrowserHistory()

class App extends Component {

  state = {
    isLoggedIn: false
  }

  componentDidMount() {
    isAuthenticated().then(response => {
      this.setState({
        isLoggedIn: response
      })
    })
  }

  handleIsLoggedIn = () => {
    console.log('handleIsLoggedIn invoked after successful login...')
    isAuthenticated().then(response => {
      this.setState({
        isLoggedIn: response
      })
    })
  }

  render() {
    return (
      <Router history={history}>
        <Container id="app-main" fluid className="d-flex flex-column">

            <Row className="align-items-center">
              <Col className="text-center">
                <img alt="Hello friend" width="8%" height="8%" src={coin} />
                <h1 className="header-app-name">MoneySaver</h1>
              </Col>
            </Row>

            <Row>
              <Col>
                <Navigation isLoggedIn={this.state.isLoggedIn} />
              </Col>
            </Row>
            <Row className="fill-height">

                <PageContent handleIsLoggedIn={this.handleIsLoggedIn.bind(this)} isLoggedIn={this.state.isLoggedIn} />

            </Row>

        </Container>

      </Router>
    )
  }
}

export default App;
