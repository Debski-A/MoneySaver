import React, { Component } from 'react'
import i18n from '../helpers/i18n'
import { getAccessToken } from '../helpers/authenticationUtils'
import Form from 'react-bootstrap/Form'
import Col from 'react-bootstrap/Col'
import Button from 'react-bootstrap/Button'
import DatePicker from 'react-datepicker'
import { withTranslation } from "react-i18next";
import "react-datepicker/dist/react-datepicker.css"
import Alert from 'react-bootstrap/Alert'

class AddIncomePage extends Component {

    state = {
        currencies: [],
        frequencies: [],
        incomeCategories: [],
        amount: '',
        currency: 0,
        frequency: 0,
        incomeCategory: 0,
        note: '',
        dateOfIncome: new Date(),
        message: '',
        errorMessage: ''
    }

    handleOnChange = (e) => {
        switch (e.target.id) {
            case "amount": {
                this.setState({
                    amount: e.target.value
                })
                break;
            }
            case "currency": {
                this.setState({
                    currency: e.target.options.selectedIndex
                })
                break;
            }
            case "frequency": {
                this.setState({
                    frequency: e.target.options.selectedIndex
                })
                break;
            }
            case "incomeCategory": {
                this.setState({
                    incomeCategory: e.target.options.selectedIndex
                })
                break;
            }
            case "note": {
                this.setState({
                    note: e.target.value
                })
                break;
            }
            case "dateOfIncome": {
                this.setState({
                    dateOfIncome: e.target.value
                })
                break;
            }
            default: break;
        }
    }

    handleDateOnChange = (date) => {
        this.setState({
            dateOfIncome: date
        })
    }

    handleOnClick = () => {
        this.addIncome()
    }

    addIncome = () => {
        // if (!isAuthenticated) authenticate()

        let incomeUpdateURL = "http://localhost/api/accounts/current/update/income"
        let currentLanguage = i18n.language
        let income = {
            "amount": this.state.amount,
            "currency": this.state.currency,
            "frequency": this.state.frequency,
            "dateOfIncome": this.state.dateOfIncome,
            "incomeCategory": this.state.incomeCategory,
            "note": this.state.note
        }
        fetch(incomeUpdateURL, {
            method: 'PUT',
            body: JSON.stringify(income),
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getAccessToken(),
                'Accept-Language': currentLanguage
            }
        })
            .then(response => response.json())
            .then(response => {
                if (!!response.errorMessage) {
                    this.setState({
                        errorMessage: response.errorMessage,
                        message: ''
                    })
                    setTimeout(() => {
                        this.setState({
                            message: '',
                            errorMessage: ''
                        });
                    }, 2000);
                }
                else if (!!response.status && response.status !== 200) {
                    throw new Error('Ststus not OK')
                }
                else {
                    this.setState({
                        message: 'Zaktualizowano',
                        errorMessage: ''
                    })
                    setTimeout(() => {
                        this.setState({
                            message: '',
                            errorMessage: ''
                        });
                    }, 2000);
                }
            })
            .catch((err) => {
                console.log(err)
                this.setState({
                    errorMessage: this.props.t('update_error'),
                    message: ''
                })
                setTimeout(() => {
                    this.setState({
                        message: '',
                        errorMessage: ''
                    });
                }, 2000);
            });
    }

    render() {
        return (
            <Form>
                <Form.Row className="sm-4 pt-4">
                    <Form.Group as={Col} sm={4}>
                        <Form.Group as={Col} sm={10}>
                            <Form.Control placeholder={this.props.t('amount_prompt')} id="amount" onChange={this.handleOnChange} />
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Form.Control as="select" id="currency" onChange={this.handleOnChange} >
                                {Object.keys(this.props.currencies).map((key) => <option key={key}>{this.props.currencies[key]}</option>)}
                            </Form.Control>
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Form.Control as="select" id="frequency" onChange={this.handleOnChange}>
                                {Object.keys(this.props.frequencies).map((key) => <option key={key}>{this.props.frequencies[key]}</option>)}
                            </Form.Control>
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Form.Control as="select" id="incomeCategory" onChange={this.handleOnChange}>
                                {Object.keys(this.props.incomeCategories).map((key) => <option key={key}>{this.props.incomeCategories[key]}</option>)}
                            </Form.Control>
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Form.Control as="textarea" rows="3" placeholder={this.props.t('note_prompt')} id="note" onChange={this.handleOnChange} />
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Alert show={!!this.state.message} variant="success">
                                {this.state.message}
                            </Alert>
                            <Alert show={!!this.state.errorMessage} variant="danger" >
                                {this.state.errorMessage}
                            </Alert>
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Button onClick={this.handleOnClick} className="ml-2" variant="secondary">{this.props.t('submit_button')}</Button>
                        </Form.Group>
                    </Form.Group>
                    <Form.Group as={Col} sm={6} className="d-flex flex-column">
                        <Form.Label>{this.props.t('date_label')}</Form.Label>
                        <DatePicker locale={this.props.i18n.language} selected={this.state.dateOfIncome} onChange={this.handleDateOnChange} inline id="dateOfIncome" />
                    </Form.Group>
                </Form.Row>
            </Form >
        )
    }

}

export default withTranslation('add_income_page')(AddIncomePage)