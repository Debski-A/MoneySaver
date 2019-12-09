import { Component } from 'react';
import { authenticate } from '../helpers/authenticationUtils'

class AuthenticateBeforeRender extends Component {
  _isMounted = false

  state = {
    isAuthenticated: false,
  }

  componentDidMount() {
    this._isMounted = true
    authenticate().then(isAuthenticated => {
      this._isMounted && this.setState({ isAuthenticated })
    })
  }

  componentWillUnmount() {
    this._isMounted = false
 }

  render() {
    return this.state.isAuthenticated ? this.props.render() : null
  }
}

export default AuthenticateBeforeRender