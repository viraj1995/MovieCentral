import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import TextField from '@material-ui/core/TextField';
import axios from 'axios/index';
import CreditCardTemplate from './CreditCardTemplate';
import Grid from '@material-ui/core/Grid';
import Modal from '@material-ui/core/Modal';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle'
import { Redirect } from "react-router-dom";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import PrimarySearchAppBar from "./searchbar";
import * as getData from "./../actions/customerAction";


const styles = theme => ({
    paper: {
        position: 'absolute',
        width: theme.spacing.unit * 50,
        backgroundColor: theme.palette.background.paper,
        boxShadow: theme.shadows[5],
        padding: theme.spacing.unit * 4,
    },
});


class Subscription extends Component {

    constructor() {
        super();
        this.state = {
            customer_id: '',
            noOfMonths: '1',
            price: '',
            months: '1',
            redirectUserDetails:false,
            open : false,
            open1 : false,
            redirectBack:false
        }
    }

    result = () => '$' + this.state.months * 10;

    updateMonths = e => this.setState({ months: +e.target.value });

    handleClickOpen = () => {
        this.setState({ open: true });
    };

    handleClose = () => {
        this.setState({ open: false });
    };

    handleChange(event) {
        this.setState({ value: event.target.value });
    }


    componentWillMount(){
        this.handleIsLoggedIn();
    }

    handleIsLoggedIn(){
        this.props.getIsLoggedIn()
        .then(res => {
          // do nothing
          this.setState({
            redirectLogin : false
          })
        })
        .catch(err => {
          // redirect to login
          this.setState({
            redirectLogin : true
          })
  
        })
      }

    handleSubmit(e) {

        let apiPayload = {};
        apiPayload.months = this.state.months;
        apiPayload.customerId = sessionStorage.getItem('userId');
        apiPayload.price = this.state.months * 10;
        this.props.subscribe(apiPayload).then(res => {
            //alert("Payment Successful");
            // do nothing

            this.setState({
                open: false,
                open1:true
            })
            
          })
          .catch(err => {
            // redirect to login
            //alert("Payment Unsuccessful");
            this.setState({
                redirectLanding: true,
                open: true,
                errMsg : "Payment unsuccessful"
            })
          });
            }
    handleRedirect(){
        this.setState({
            
          redirectBack: true
        });
    }

    handleLastFlow(){
        if(!this.props.match.params.movie_id){
            this.setState({
                redirectUserDetails: true
            })
        }
        else{
            this.setState({
                redirectBack: true
            })
        }
    }

    render() {
        const classes = this.props;
        //alert();
        let routeUrl = "/movie-details/"+this.props.match.params.movie_id;
        if(this.state.redirectBack)
        return (<Redirect to={{
            pathname: routeUrl
        }} />)

        if(this.state.redirectLanding)
        return (<Redirect to={{
            pathname: '/landing'
        }} />)

        if(this.state.redirectLogin)
        return (<Redirect to={{
            pathname: '/login'
        }} />)
        if(this.state.redirectUserDetails)
        return (<Redirect to={{
            pathname: '/userDetails'
        }} />)
        
        
        return (<div>
            <Grid container justify="center">
                <h4>Subscribe now for just $10/month.</h4>
            </Grid>
            <Grid container justify="center">
                <h5> Number of months:</h5></Grid>
            <Grid container justify="center">
                <select onChange={this.updateMonths} value={this.state.months}>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                    <option value="11">11</option>
                    <option value="12">12</option>
                </select></Grid>
            <Grid container justify="center">
                Your total is: {this.result()}. Enter your card details below to proceed.
        </Grid>
            <CreditCardTemplate />
            <Grid container justify="center">
                <Button size="small" onClick={this.handleClickOpen}>Pay</Button>
            </Grid>
            <Dialog
                open={this.state.open}
                onClose={this.handleClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
                disableEscapeKeyDown = {true}
          disableBackdropClick = {true}
            >
                <DialogTitle id="alert-dialog-title">{"Are you sure?"}</DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        You will be charged once you click Subscribe Now.
            </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={this.handleClose} color="primary">
                        Cancel
            </Button>
                    <Button onClick={this.handleSubmit.bind(this)} color="primary" autoFocus>
                        Subscribe Now
            </Button>
                </DialogActions>
            </Dialog>
        <Dialog
          open={this.state.open1}
          onClose={this.handleClose}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description"
          disableEscapeKeyDown = {true}
          disableBackdropClick = {true}
        >
          <DialogTitle id="alert-dialog-title">{"Payment status"}</DialogTitle>
          <DialogContent>
            <DialogContentText id="alert-dialog-description">
              Successful
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={this.handleLastFlow.bind(this)} color="primary">
              Okay
            </Button>
          </DialogActions>
        </Dialog>




        </div>
        );
    }
}


// export default Subscription;


function mapStateToProps(state){
    return{
        customerData : state.CustomerReducer
    };
}

function mapDispatchToProps(dispatch){
    return bindActionCreators(getData,dispatch)

}


export default connect(mapStateToProps,mapDispatchToProps)(Subscription);
