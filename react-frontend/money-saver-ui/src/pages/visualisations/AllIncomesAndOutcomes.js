import React, { Component } from 'react'
import { VictoryChart, VictoryLine, VictoryTheme, VictoryLabel, VictoryAxis } from 'victory'
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
        console.log(new Date().toLocaleDateString())
        return (
            <Container fluid className="d-flex fill-height">
                <VictoryChart la style={{ parent: { maxWidth: "60%", maxHeight: "70%" } }} theme={VictoryTheme.grayscale}>
                    <VictoryAxis
                        style={{
                            axis: { stroke: '#000' },
                            axisLabel: { fontSize: 10 },
                            ticks: { stroke: '#000' },
                            grid: { stroke: '#B3E5FC', strokeWidth: 0.5 }
                        }} dependentAxis
                    />
                    <VictoryAxis tickCount={4}
                        style={{
                            axis: { stroke: '#000' },
                            axisLabel: { fontSize: 10 },
                            ticks: { stroke: '#000' },
                            tickLabels: { fontSize: 10, padding: 1, angle: 45, verticalAnchor: 'middle', textAnchor: 'start' }
                        }}
                    />
                    {/* MIESIĄCE SA 0-11 */}
                    <VictoryLine data={[
                        { x: new Date().toLocaleDateString(), y: -5 },
                        { x: new Date(2020, 0, 6).toLocaleDateString(), y: 3 },
                        { x: new Date(2019, 10, 11).toLocaleDateString(), y: 123 },
                        { x: new Date(2017, 2, 13).toLocaleDateString(), y: 23 },
                        { x: new Date(2018, 12, 6).toLocaleDateString(), y: 343 },
                        { x: new Date(2019, 4, 4).toLocaleDateString(), y: 123 },
                        { x: new Date(2019, 7, 15).toLocaleDateString(), y: 1020 },
                        { x: new Date(2017, 12, 6).toLocaleDateString(), y: -220 },
                        { x: new Date(2019, 8, 23).toLocaleDateString(), y: 666 },
                    ]}></VictoryLine>
                    <VictoryLine data={[
                        { x: new Date().toLocaleDateString(), y: 23 },
                        { x: new Date(2020, 2, 6).toLocaleDateString(), y: 3 },
                    ]}></VictoryLine>
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