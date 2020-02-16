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
import Footer from './Footer'

export const history = createBrowserHistory()

class App extends Component {

  state = {
    isLoggedIn: false
  }

  componentDidMount() {
    isAuthenticated().then(resp => {
      this.setState({
        isLoggedIn: resp
      })
    })
  }

  handleIsLoggedIn = () => {
    console.log('handleIsLoggedIn invoked after successful login...')
    isAuthenticated().then(resp => {
      this.setState({
        isLoggedIn: resp
      })
    })
  }

  render() {
    return (
      <Router history={history}>
        <Container fluid className="d-flex flex-column " style={{height: '94vh'}}>
          <Container>
            <Row className="align-items-center">
              <Col className="text-center">
                <img alt="Hello friend" width="15%" height="15%" src={coin} />
                <h1 className="header-app-name">MoneySaver</h1>
              </Col>
            </Row>
          </Container>
          <Container fluid className="d-flex flex-column fill-height">
            <Row>
              <Col>
                <Navigation isLoggedIn={this.state.isLoggedIn} />
              </Col>
            </Row>
            <Row className="fill-height">
              <Col className="d-flex flex-column fill-height">
                <PageContent handleIsLoggedIn={this.handleIsLoggedIn.bind(this)} isLoggedIn={this.state.isLoggedIn} />
              </Col>
            </Row>
          </Container>
        </Container>
        <Footer>
          <span>Project by Adam Dębski</span>
        </Footer>
      </Router>
    )
  }
}

export default App;
