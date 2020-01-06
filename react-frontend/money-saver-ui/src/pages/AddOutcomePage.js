import React, { Component } from 'react'
import i18n from '../helpers/i18n'
import { authenticate, isAuthenticated, getAccessToken } from '../helpers/authenticationUtils'
import Form from 'react-bootstrap/Form'
import Col from 'react-bootstrap/Col'
import Button from 'react-bootstrap/Button'
import DatePicker, {registerLocale} from 'react-datepicker'
import { withTranslation } from "react-i18next";
import "react-datepicker/dist/react-datepicker.css"
import pl from "date-fns/locale/pl";
registerLocale('pl', pl)

class AddOutcomePage extends Component {

    state = {
        startDate: new Date(),
        currencies: [],
        frequencies: [],
        outcomeCategories: []
    }

    componentDidMount() {
        this.getDropdownValues()
    }

    getDropdownValues = () => {
        if (!isAuthenticated) authenticate()

        let incomeCategoriesUrl = "http://localhost/api/accounts/dropdown_values"
        let currentLanguage = i18n.language
        fetch(incomeCategoriesUrl, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getAccessToken(),
                'Accept-Language': currentLanguage
            }
        })
            .then(response => response.json())
            .then(response => {
                let { currencies, frequencies, outcomeCategories } = response
                this.setState({
                    currencies: currencies,
                    frequencies: frequencies,
                    outcomeCategories: outcomeCategories
                })
            })
            .catch(err => {
                console.log(err)
            })
    }

    handleOnChange = (date) => {
        console.log(date)
        this.setState({
            startDate: date
        })
    }

    render() {
        return (
            <Form>
                <Form.Row className="sm-4 pt-4">
                    <Form.Group as={Col} sm={4}>
                        <Form.Group as={Col} sm={10}>
                            <Form.Control placeholder={this.props.t('amount_prompt')} />
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Form.Control as="select" >
                                {this.state.currencies.map((value, index) => <option key={index}>{value}</option>)}
                            </Form.Control>
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Form.Control as="select" >
                                {this.state.frequencies.map((value, index) => <option key={index}>{value}</option>)}
                            </Form.Control>
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Form.Control as="select" >
                                {this.state.outcomeCategories.map((value, index) => <option key={index}>{value}</option>)}
                            </Form.Control>
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Form.Control as="textarea" rows="3" placeholder={this.props.t('note_prompt')} />
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Button className="ml-2" variant="secondary">{this.props.t('submit_button')}</Button>
                        </Form.Group>
                    </Form.Group>
                    <Form.Group as={Col} sm={6} className="d-flex flex-column">
                        <Form.Label>{this.props.t('date_label')}</Form.Label>
                        <DatePicker locale={this.props.i18n.language} selected={this.state.startDate} onChange={this.handleOnChange} inline />
                    </Form.Group>
                </Form.Row>
            </Form >
        )
    }

    componentDidUpdate(prevProps) {
        if (this.props.t !== prevProps.t) {
            this.getDropdownValues()
        }
    }
}

export default withTranslation('add_outcome_page')(AddOutcomePage)