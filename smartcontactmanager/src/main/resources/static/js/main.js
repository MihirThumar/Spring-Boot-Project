console.log("This is script fiel");

const toggleSidebar = () => {

	if ($(".sidebar").is(":visible")) {
		//true
		//Close code
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");

	} else {
		//false
		//show code
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}

}

function deleteContact(cId) {
	swal({
		title: "Are you sure?",
		text: "you want to Delete this contact..",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				window.location = "/user/delete/" + cId;
			} else {
				swal("Your Contact is safe !")
			}
		});
}

const search = () => {
	//	console.log("seraching......");

	let query = $("#search-input").val();

	if (query == '') {
		$(".search_result").hide();

	} else {
		//search
		console.log(query);

		//sending request to server

		let url = `http://localhost:8181/search/${query}`;

		fetch(url).then((response) => {
			return response.json();
		}).then((data) => {

			//data......
			//console.log(data);

			let text = `<div class='list-group'>`;

			data.forEach((contacts)=>{
				text+=`<a href='/user/${contacts.cId}/contact' class='list-group-item list-group-item-action'>
					${contacts.name}
				</a>`
			});

			text+=`</div>`;

			$(".search_result").html(text);
			$(".search_result").show();

		});

	}

}

//first request - to server to create order

const paymentStart = () => {
	console.log("payment started....");
	let amount = $("#payment_field").val()
	console.log(amount);
	if(amount == "" || amount == null)
	{
		swal("Failed !!","amount is required !! ","error");
		return;
	}

	// Code..
	// using ajax to send request to server to create order - jquery

	$.ajax(
		{
			url:"/user/create_order",
			data:JSON.stringify({ amount: amount, info: "order_request" }),
			contentType:"application/json",
			type:"POST",
			dataType:"json",	
			success:function(response){
				//invoke when success
				console.log(response);
				if(response.status == "created")
				{
					//open payment form
					let options={
						key:'rzp_test_CasvULeibjcRSx',
						amount:response.amount,
						currency:'INR',
						name:'Smart Contact Manger',
						description:'Donation',
						image:'https://lh3.googleusercontent.com/a-/AFdZucrZ_Jl4Tay4O8oU5rMg3pFJVlTs3VKKf8mXKkmA-w=s96-c-rg-br100',
						order_id:response.id,
						handler:function(response)
						{
							console.log(response.razorpay_payment_id);
							console.log(response.razorpay_order_id);
							console.log(response.razorpay_signature)
							console.log('congrates !! payment successfull !!');
							
							updatePaymentOnServer(
								response.razorpay_payment_id,
								response.razorpay_order_id,
								"paid"
							);

						},
						"prefill": {
							"name": "",
							"email": "",
							"contact": ""
						},
						"notes": {
							"address": "Mihir Thumar"
						},
						"theme": {
							"color": "#3399cc"
						}
					};

					let rzp = new Razorpay(options);

					rzp.on('payment.failed', function (response){
						console.log(response.error.code);
						console.log(response.error.description);
						console.log(response.error.source);
						console.log(response.error.step);
						console.log(response.error.reason);
						console.log(response.error.metadata.order_id);
						console.log(response.error.metadata.payment_id);
						swal("Failed !!","Oops payment failed !!","error");
				});

					rzp.open();
				}
			},
			error:function(eroor){
				//invoke when error
				console.log(eroor)
				swal("Failed !!","something went wrong !!","error");
			}
		}
	)

};	

//
function updatePaymentOnServer(payment_id,order_id,status)
{
	$.ajax({
		url:"/user/update_order",
		data: JSON.stringify({payment_id: payment_id, order_id: order_id, status:status}),
		contentType : "application/json",
		type: "POST",
		dataType: "json",
		success:function(response){
			swal("Good Job !","congrates !! Payment successful !!","success");
		},
		error:function(eroor){
			swal("Failed !!"," your payment is succesfull but we did not get on server, we will contact you as soon as possible","error")
		},
	});
}
