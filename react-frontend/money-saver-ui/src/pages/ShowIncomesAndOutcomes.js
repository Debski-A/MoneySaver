import React, { Component } from 'react'
import i18n from '../helpers/i18n'
import { getAccessToken } from '../helpers/authenticationUtils'
import { withTranslation } from "react-i18next";
import "react-datepicker/dist/react-datepicker.css"
import Table from 'react-bootstrap/Table'
import Col from 'react-bootstrap/Col'
import Row from 'react-bootstrap/Row'
import Button from 'react-bootstrap/Button'
import Loader from 'react-loader'
import Nav from 'react-bootstrap/Nav'

class ShowIncomesAndOutcomes extends Component {

    pagePlusOneRow = 19

    state = {
        startIndex: 0,
        currentLanguage: i18n.language,
        isDataLoaded: false,
        budget: []
    }

    getBudgetData = async () => {
        let currentBudgetURL = "http://localhost/api/accounts/current/budget?startIndex=" + this.state.startIndex + "&endIndex=" + (this.state.startIndex + this.pagePlusOneRow)
        let currentLanguage = i18n.language
        await fetch(currentBudgetURL, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getAccessToken(),
                'Accept-Language': currentLanguage
            }
        })
            .then(response => response.json())
            .then(response => {
                if (!!response.errorMessage || !!response.error) {
                    console.log(response.errorMessage)
                    console.log(response.error)
                }
                else if (!!response.status && response.status !== 200) {
                    throw new Error('Ststus not OK')
                }
                else {
                    this.setState({
                        budget: response,
                        isDataLoaded: true
                    })
                }
            })
            .catch((err) => {
                console.log(err)
            });
    }

    deleteBudget = (budgetType, uuid) => {
        console.log(budgetType)
        console.log(uuid)
        let deleteBudgetURL = "http://localhost/api/accounts/current/budget/delete"
        let budget = {
            "budgetType": budgetType,
            "uuid": uuid
        }
        fetch(deleteBudgetURL, {
            method: 'DELETE',
            body: JSON.stringify(budget),
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getAccessToken()
            }
        })
            .then(this.getBudgetData)
    }

    constructTable = () => {
        let id = this.state.startIndex + 1
        let index = 0
        return this.state.budget.map(i => {
            if (index < 18) {
                ++index
                return (
                    <tr key={id}>
                        <td>{id++}</td>
                        <td>{i.budgetTypeDescription}</td>
                        <td>{i.amount}</td>
                        <td>{i.currencyDescription}</td>
                        <td>{i.categoryDescription}</td>
                        <td>{i.frequencyDescription}</td>
                        <td>{i.date}</td>
                        <td>{i.note}</td>
                        <td onClick={() => this.deleteBudget(i.budgetType, i.uuid)}>
                        <Nav.Link className="m-0 p-0">{this.props.t('delete_budget')}</Nav.Link>
                        </td>
                    </tr>
                    // HMMMM jak przekazac i.budgetType i i.uuid do onCLick

                )   
            }
            return null
        })
    }

    prevButtonOnCLick = () => {
            this.setState((prevState) => ({
                startIndex: prevState.startIndex - 18,
                isDataLoaded: false
            }), this.getBudgetData)

    }

    nextButtonOnClick = () => {
        this.setState((prevState) => ({
            startIndex: prevState.startIndex + 18,
            isDataLoaded: false
        }), this.getBudgetData)
    }

    render() {
        if (!this.state.isDataLoaded) return <Loader />
        return (
            <>
                <Table striped bordered hover className="pb-0 mb-0" size="sm">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>{this.props.t('budget_type')}</th>
                            <th>{this.props.t('amount')}</th>
                            <th>{this.props.t('currency')}</th>
                            <th>{this.props.t('categoryDescription')}</th>
                            <th>{this.props.t('frequencyDescription')}</th>
                            <th>{this.props.t('date')}</th>
                            <th>{this.props.t('note')}</th>
                            <th>X</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.constructTable()}
                    </tbody>
                </Table>
                <Row className="m-0 p-0">
                    {this.state.startIndex < 18 && this.state.budget.length < 19 && <></>}
                    {this.state.startIndex < 18 && this.state.budget.length >= 19 &&
                        <>
                            <Col md={8}sm={8} xs={8} ></Col>
                            <Col md={4}sm={4} xs={4} className="m-0 text-right">
                                <Button onClick={this.nextButtonOnClick} size="sm" variant="secondary">{this.props.t('next_page')}</Button>
                            </Col>
                        </>
                    }
                    {this.state.startIndex >= 18 && this.state.budget.length < 19 && 
                        <Col md={4} sm={4} xs={4} className="m-0 ">
                            <Button onClick={this.prevButtonOnCLick} size="sm" variant="secondary">{this.props.t('prev_page')}</Button>
                        </Col>
                    }
                    {this.state.startIndex >= 18 && this.state.budget.length >= 19 && 
                        <>
                            <Col md={4} sm={4} xs={4} className="m-0 ">
                                <Button onClick={this.prevButtonOnCLick} size="sm" variant="secondary">{this.props.t('prev_page')}</Button>
                            </Col>
                            <Col md={4}sm={4} xs={4} ></Col>
                            <Col md={4}sm={4} xs={4} className="m-0 text-right">
                                <Button onClick={this.nextButtonOnClick} size="sm" variant="secondary">{this.props.t('next_page')}</Button>
                            </Col>
                        </>
                    }
                </Row>
            </>
        )
    }

    componentDidMount() {
        this.getBudgetData()
    }

    componentDidUpdate() {
        if (this.state.currentLanguage !== i18n.language) {
            this.getBudgetData()
            this.setState({
                currentLanguage: i18n.language,
                isDataLoaded: false
            })
        }
    }

}

export default withTranslation('show_incomes_and_outcomes')(ShowIncomesAndOutcomes)