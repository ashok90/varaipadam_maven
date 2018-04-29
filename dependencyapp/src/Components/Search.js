import React, { Component } from 'react'
import axios from 'axios';

class Search extends Component {
    constructor(props) {
        super(props);
        this.state = { 
            value: '',
            message: ''
         };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({ value: event.target.value });
    }

    clearForm() {
        this.setState({ value: '' });
    }

    handleSubmit(event) {
        var self = this;
        console.log('A name was submitted: ' + this.state.value);
        event.preventDefault();
        axios.get('http://localhost:9090/get_dependencies', {
            params: {
                name: this.state.value
            }
        })
            .then(function (response) {
                console.log(response);
                self.setState({ message: 'data' });
            })
            .catch(function (error) {
                console.log(error);
            });
        this.clearForm();
    }


    render() {
        return (
            <div className="container">
            <br/><br/><br/>
                <div align="center">
                    <form onSubmit={this.handleSubmit}>
                        <label>
                            Enter jar name: &nbsp;
                        <input type="text" value={this.state.value} onChange={this.handleChange} />
                        </label>
                        &nbsp;
                        <input type="submit" value="Submit" />
                    </form>
                </div>
                <div>
                   <p> {this.state.message} </p>
                </div>
            </div>
        )
    }
}

export default Search
