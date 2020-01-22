import React, { Component } from 'react'
import { VictoryChart, VictoryLine, VictoryTheme } from 'victory'
import { withTranslation } from "react-i18next";
import DatePicker from 'react-datepicker'
import Col from 'react-bootstrap/Col'
import Container from 'react-bootstrap/Container'

class AllIncomesAndOutcomes extends Component {

    state = {
        startDate: new Date(),
        endDate: new Date()
    }

    handleStartDate = (date) => {
        this.setState({
            startDate: date
        })
    }

    handleEndDate = (date) => {
        this.setState({
            endDate: date
        })
    }

    render() {
        return (
            <Container fluid className="d-flex fill-height">
                <VictoryChart style={{ parent: { maxWidth: "60%", maxHeight: "70%" } }} theme={VictoryTheme.grayscale}>
                    <VictoryLine ></VictoryLine>
                </VictoryChart>
                <Col sm={2} className="flex-column mt-3">
                <DatePicker
                    selected={this.state.startDate}
                    onChange={this.handleStartDate}
                    selectsStart
                    startDate={this.state.startDate}
                    endDate={this.state.endDate}
                />
                <DatePicker
                    selected={this.state.endDate}
                    onChange={this.handleEndDate}
                    selectsEnd
                    startDate={this.state.startDate}
                    endDate={this.state.endDate}
                    minDate={this.state.startDate}
                />
                {/* <DatePicker className="p-2" locale={this.props.i18n.language} inline id="date" /> */}
                </Col>
            </Container>
        )
    }
}

export default withTranslation('visualisations')(AllIncomesAndOutcomes)