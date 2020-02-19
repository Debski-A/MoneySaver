import React, { Component } from 'react'
import Col from "react-bootstrap/Col"
import Nav from 'react-bootstrap/Nav'
import NavDropdown from 'react-bootstrap/NavDropdown'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPiggyBank } from '@fortawesome/free-solid-svg-icons'
import { faMoneyBillWave } from '@fortawesome/free-solid-svg-icons'
import { faChartLine } from '@fortawesome/free-solid-svg-icons'
import HomePageContent from '../layouts/HomePageContent'
import { NavLink } from 'react-router-dom'
import { withTranslation } from "react-i18next"
import { registerLocale } from 'react-datepicker'
import Loader from 'react-loader'
import pl from "date-fns/locale/pl"
import i18n from '../helpers/i18n'
import { getAccessToken } from '../helpers/authenticationUtils'
registerLocale('pl', pl)

class MainPage extends Component {

    state = {
        currencies: [],
        frequencies: [],
        incomeCategories: [],
        outcomeCategories: [],
        isDataLoaded: false,
        currentLanguage: i18n.language
    }

    componentDidMount() {
        this.getDropdownValues()
    }

    getDropdownValues = async () => {
        // if (!isAuthenticated) authenticate()

        let dropDownValuesURL = "http://localhost/api/accounts/dropdown_values"
        let currentLanguage = i18n.language
        await fetch(dropDownValuesURL, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getAccessToken(),
                'Accept-Language': currentLanguage
            }
        })
            .then(response => response.json())
            .then(response => {
                let { currencies, frequencies, incomeCategories, outcomeCategories } = response
                this.setState({
                    currencies: currencies,
                    frequencies: frequencies,
                    incomeCategories: incomeCategories,
                    outcomeCategories: outcomeCategories,
                    isDataLoaded: true
                })
            })
            .catch(err => {
                console.log(err)
            })
    }

    render() {
        if (!this.state.isDataLoaded) return <Loader />
        return (
            <>
                <Col className="flex-column" sm={2}>
                    <Nav fill variant="pills" className="bg-light  text-dark h-100" >
                        <Nav.Item className="w-100">
                        <FontAwesomeIcon size="4x" icon={faPiggyBank} color="#6c757d" />
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link as={NavLink} exact to="/main/income/add" className="bg-secondary text-white">{this.props.t('income_button')}</Nav.Link>
                        </Nav.Item>
                        <Nav.Item className="w-100">
                        <FontAwesomeIcon size="4x" icon={faMoneyBillWave} color="#6c757d" />
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link as={NavLink} exact to="/main/outcome/add" className="bg-secondary text-white">{this.props.t('outcome_button')}</Nav.Link>
                        </Nav.Item>
                        <Nav.Item className="w-100">
                        <FontAwesomeIcon size="4x" icon={faChartLine} color="#6c757d" />
                        </Nav.Item>
                        <NavDropdown bsPrefix="nav-link bg-secondary text-white" title={this.props.t('visualisation_button')} >
                            <NavDropdown.Item as={NavLink} exact to="/main/visualization/1" className="bg-secondary text-white" onClick={() => console.log('viz1 clicked')}>{this.props.t('periodic_summary')} </NavDropdown.Item>
                            <NavDropdown.Divider />
                            <NavDropdown.Item className="bg-secondary text-white" onClick={() => console.log('viz2 clicked')}>Viz2</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                </Col>
                <Col sm={10}>
                    <HomePageContent {...this.state} />
                </Col>
            </>
        )
    }

    componentDidUpdate() {
        if (this.state.currentLanguage !== i18n.language) {
            this.getDropdownValues()
            this.setState({
                currentLanguage: i18n.language
            })
        }

    }
}


export default withTranslation('mainpage')(MainPage)
