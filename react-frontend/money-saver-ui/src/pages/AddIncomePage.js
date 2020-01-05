import React, { Component } from 'react'
import i18n from '../helpers/i18n'
import { authenticate, isAuthenticated, getAccessToken } from '../helpers/authenticationUtils'
import Form from 'react-bootstrap/Form'
import Col from 'react-bootstrap/Col'
import Button from 'react-bootstrap/Button'
import DatePicker from 'react-datepicker'
import "react-datepicker/dist/react-datepicker.css"

class AddIncomePage extends Component {

    state = {
        startDate: new Date()
    }

    componentDidMount() {
        this.getIncomeCategories()
        //i tutaj state dla categories
    }

    getIncomeCategories = () => {
        if (!isAuthenticated) authenticate()

        let incomeCategoriesUrl = "http://localhost/api/accounts/income/all"
        let currentLanguage = i18n.language
        fetch(incomeCategoriesUrl, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getAccessToken(),
                'Accept-Language': currentLanguage
            }
        })
            .then(response => response.json())
            .then(response => console.log(response))
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
                            <Form.Control placeholder="kwota" />
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Form.Control as="select" >
                                <option>dolar</option>
                                <option>ajsy</option>
                            </Form.Control>
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Form.Control as="select" >
                                <option>Jednorazowo</option>
                                <option>Co miesiąc</option>
                                <option>Co rok</option>
                            </Form.Control>
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Form.Control as="select" >
                                <option>jedzenie</option>
                                <option>prezent</option>
                                <option>Co rok</option>
                            </Form.Control>
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                            <Form.Control as="textarea" rows="3" placeholder="notatka" />
                        </Form.Group>
                        <Form.Group as={Col} sm={10}>
                        <Button className="ml-2" variant="secondary">Zapisz</Button>
                        </Form.Group>
                    </Form.Group>
                    <Form.Group as={Col} sm={6} className="d-flex flex-column">
                        <Form.Label>Data przychodu</Form.Label>
                        <DatePicker selected={this.state.startDate} onChange={this.handleOnChange} inline />
                    </Form.Group>
                </Form.Row>
            </Form >
        )
    }

    componentDidUpdate() {
        this.getIncomeCategories()
    }
}

export default AddIncomePage