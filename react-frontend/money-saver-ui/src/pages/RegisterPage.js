import React, { Component } from "react"
import SubmitButton from "../components/SubmitButton"
import Form from 'react-bootstrap/Form'
import Alert from 'react-bootstrap/Alert'
import Col from "react-bootstrap/Col"
import Row from "react-bootstrap/Row"
import i18n from '../helpers/i18n'
import { withTranslation } from "react-i18next";

class RegisterPage extends Component {
  state = {
    username: '',
    password: '',
    email: '',
    errorMessage: '',
    successMessage: ''
  }

  handleOnChange = (e) => {
    switch (e.target.id) {
      case "username_input": {
        this.setState({
          username: e.target.value
        })
        break;
      }
      case "password_input": {
        this.setState({
          password: e.target.value
        })
        break;
      }
      case "email_input": {
        this.setState({
          email: e.target.value
        })
        break;
      }
      default: break;
    }
  }

  handleOnClick = () => {
    let apiBaseUrl = "http://localhost:80/api/accounts/create"
    let currentLanguage = i18n.language
    let registerCredentials = {
      "password": this.state.password,
      "username": this.state.username,
      "email": this.state.email
    }
    fetch(apiBaseUrl, {
      method: 'POST',
      body: JSON.stringify(registerCredentials),
      headers: {
        'Content-Type': 'application/json',
        'Accept-Language': currentLanguage
      }
    })
    .then(response => {
      if (!!response.ok) {
        console.log('Success!')
        let successMessage = this.props.t('register_success')
        this.setState({
          successMessage: successMessage
        })
        setTimeout(() => {
          this.setState({
            successMessage: ''
          });
        }, 2000)
      }
      else { 
        response.json()
        .then(result => {
          console.log(result.errorMessage)
          this.setState({
            errorMessage: result.errorMessage
          })
          setTimeout(() => {
            this.setState({
              errorMessage: ''
            });
          }, 2000);
        })
      }
    })
    .catch(err => {
      console.log(err)
      let errorMessage = this.props.t('register_error')
      this.setState({
        errorMessage: errorMessage
      })
      setTimeout(() => {
        this.setState({
          errorMessage: ''
        });
      }, 2000);
    })
  }

  render() {
    return (
      <Form className="mt-2 ml-2 w-100">
        <Form.Group as={Row}>
          <Col sm={4}>
            <Form.Control id="username_input" onChange={this.handleOnChange} placeholder={this.props.t('username_prompt')} />
          </Col>
        </Form.Group>
        <Form.Group as={Row}>
          <Col sm={4} >
            <Form.Control id="password_input" onChange={this.handleOnChange} type="password" placeholder={this.props.t('password_prompt')} />
          </Col>
        </Form.Group>
        <Form.Group as={Row}>
          <Col sm={4}>
            <Form.Control id="email_input" onChange={this.handleOnChange} type="email" placeholder={this.props.t('email_prompt')} />
          </Col>
        </Form.Group>
        <SubmitButton onClick={this.handleOnClick} value={this.props.t('submit_button')} />
        <Row className="pt-2">
          <Col sm={4}>
            <Alert show={!!this.state.errorMessage} variant="danger">
              {this.state.errorMessage}
            </Alert>
            <Alert show={!!this.state.successMessage} variant="success">
              {this.state.successMessage}
            </Alert>
          </Col>
        </Row>
      </Form>
    );
  }
}

export default withTranslation('register_page')(RegisterPage);